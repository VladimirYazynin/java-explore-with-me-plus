package ru.practicum.ewm.compilation.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationMapper {
    private final EventRepository repository;
    private final EventMapper mapper;

    public Compilation toCompilation(NewCompilationDto compilation) {
        List<Event> events = new ArrayList<>();
        if (compilation.getEvents() != null) {
            events = repository.findAllById(compilation.getEvents());
        }
        return new Compilation(
                compilation.getId(),
                events,
                compilation.getPinned(),
                compilation.getTitle());
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        return new CompilationDto(
                compilation.getId(),
                compilation.getEvents().stream().map(mapper::toEventShortDto).collect(Collectors.toList()),
                compilation.getPinned(),
                compilation.getTitle());
    }
}
