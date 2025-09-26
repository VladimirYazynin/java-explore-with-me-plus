package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.enums.State;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.AccessException;
import ru.practicum.ewm.exception.DataViolationException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.enums.State.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public ParticipationRequestDto create(Long userId, Long eventId) {
        log.info("create({}, {})", userId, eventId);
        User user = userValidation(userId);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не найдено"));
        if (!event.getState().equals(PUBLISHED)) {
            throw new DataViolationException("Событие не найдено");
        }
        if (requestRepository.existsByEventAndRequester(event, user)) {
            throw new DataViolationException("Нельзя отправить запрос повторно");
        }
        if (event.getInitiator().equals(user)) {
            throw new DataViolationException("Вы уже участвуете в событии, будучи организатором");
        }
        if (!event.getRequestModeration()
                && event.getParticipantLimit() == requestRepository.findByEventId(eventId).size()) {
            throw new DataViolationException("Нет мест для участия в мероприятии");
        }
        ParticipationRequest request = new ParticipationRequest();
        request.setCreated(LocalDateTime.now());
        request.setEvent(event);
        request.setRequester(user);
        State status = event.getRequestModeration() ? PENDING : CONFIRMED;
        request.setStatus(event.getParticipantLimit() == 0 ? CONFIRMED : status);
        ParticipationRequest savedRequest = requestRepository.save(request);
        log.info("Запрос создан: {}", savedRequest);
        return requestMapper.toParticipationRequestDto(savedRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> get(Long userId) {
        log.info("get({})", userId);
        User user = userValidation(userId);
        List<ParticipationRequestDto> requests = requestRepository.findAllByRequester(user)
                .stream().map(requestMapper::toParticipationRequestDto).collect(Collectors.toList());
        log.info("По запросу пользователя возвращён список заявок: {}", requests);
        return requests;
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancel(Long userId, Long requestId) {
        log.info("cancel({}, {})", userId, requestId);
        User user = userValidation(userId);
        ParticipationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Данные не найдены"));
        if (!request.getRequester().equals(user)) {
            throw new AccessException("Нет доступа");
        }
        request.setStatus(CANCELED);
        ParticipationRequest savedRequest = requestRepository.save(request);
        log.info("Заявка на участие в событии отменена: {}", savedRequest);
        return requestMapper.toParticipationRequestDto(savedRequest);
    }

    private User userValidation(Long userId) {
        log.info("userValidation({})", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        log.info("Зарос на поиск пользователя прошёл успешно: {}", user);
        return user;
    }
}
