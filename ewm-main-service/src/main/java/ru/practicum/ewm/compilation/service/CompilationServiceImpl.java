package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDto;
import ru.practicum.ewm.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper mapper;

    @Override
    @Transactional
    public CompilationDto create(NewCompilationDto compilation) {
        log.info("create({})", compilation);
        Compilation thisCompilation = mapper.toCompilation(compilation);
        titleValidation(compilation.getTitle());
        thisCompilation.setPinned(compilation.getPinned() != null ? compilation.getPinned() : false);
        Compilation saved = compilationRepository.save(thisCompilation);
        log.info("Добавлена подборка: {}", saved);
        return mapper.toCompilationDto(saved);
    }

    @Override
    @Transactional
    public CompilationDto update(Long compId, UpdateCompilationDto compilation) {
        log.info("update({}, {})", compId, compilation);
        Compilation foundCompilation = findById(compId);
        titleValidation(compilation.getTitle());
        if (compilation.getTitle() != null) {
            foundCompilation.setTitle(compilation.getTitle());
        }
        if (compilation.getEvents() != null) {
            List<Long> eventsIds = compilation.getEvents();
            List<Event> events = eventRepository.findAllById(Stream.concat(eventsIds.stream(),
                            foundCompilation.getEvents().stream()
                                    .map(Event::getId).toList().stream())
                    .filter(eventsIds::contains).collect(Collectors.toList()));
            foundCompilation.setEvents(events);
        }
        if (compilation.getPinned() != null) {
            foundCompilation.setPinned(compilation.getPinned());
        }
        Compilation saved = compilationRepository.save(foundCompilation);
        log.info("Подборка обновлена: {}", saved);
        return mapper.toCompilationDto(saved);
    }

    @Override
    @Transactional
    public void delete(Long compId) {
        log.info("delete({})", compId);
        Compilation compilation = findById(compId);
        compilationRepository.delete(compilation);
        log.info("Подборка удалена: {}", compilation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getAll(Boolean pinned, int from, int size) {
        log.info("getAll({}, {}, {})", pinned, from, size);
        PageRequest page = PageRequest.of(from, size);
        List<CompilationDto> compilations;
        if (pinned != null && pinned) {
            compilations = compilationRepository.findAllByPinned(pinned, page)
                    .stream().map(mapper::toCompilationDto).toList();
        } else {
            compilations = compilationRepository.findAll(page)
                    .stream().map(mapper::toCompilationDto).collect(Collectors.toList());
        }
        log.info("Возвращён список с подборками: {}", compilations);
        return compilations;
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getById(Long compId) {
        log.info("getById({})", compId);
        Compilation compilation = findById(compId);
        log.info("Возвращена подборка по запросу пользователя: {}", compilation);
        return mapper.toCompilationDto(compilation);
    }

    private Compilation findById(Long compId) {
        log.info("findById({})", compId);
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка не найдена"));
        log.info("Подборка найдена: {}", compilation);
        return compilation;
    }

    private void titleValidation(String title) {
        if (compilationRepository.existsByTitle(title)) {
            throw new ValidationException("Такая подборка уже существует");
        }
    }
}
