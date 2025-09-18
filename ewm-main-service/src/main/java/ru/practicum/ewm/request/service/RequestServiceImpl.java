package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.Status;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.exceptions.EventNotFound;
import ru.practicum.ewm.event.interfaces.EventService;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.exception.RequestNotExists;
import ru.practicum.ewm.request.exception.RequestNotFound;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.exception.UserNotFound;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.event.State.PUBLISHED;
import static ru.practicum.ewm.event.Status.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final UserRepository userRepository;
    private final EventService eventService;

    @Override
    public ParticipationRequestDto create(Long userId, Long eventId) throws EventNotFound, RequestNotExists, UserNotFound {
        log.debug("create({}, {})", userId, eventId);
        User user = userValidation(userId);
        EventFullDto event = eventService.findEvent(userId, eventId);
        if (!event.getState().equals(PUBLISHED)) {
            //возможно перенесется в метод сервиса findEvent или надо использовать другой метод
            throw new EventNotFound("Событие не найдено", "Событие не опубликовано");
        }
        if (requestRepository.existsByEventIdAndRequester(event.getId(), user)) {
            throw new RequestNotExists("Нельзя отправить запрос повторно", "уже добавлено");
        }
        if (event.getInitiator().getId().equals(user.getId())) {
            throw new RequestNotExists("Вы уже участвуете в событии, будучи организатором", "-");
        }
        if (!event.isRequestModeration()
            && event.getParticipantLimit() == requestRepository.findByEventId(eventId).size()) {
            throw new RequestNotExists("Нет мест для участия в мероприятии", "-");
        }
        ParticipationRequest request = new ParticipationRequest();
        request.setCreated(LocalDateTime.now());
        request.setEventId(event.getId());
        request.setRequester(user);
        Status status = event.isRequestModeration() ? PENDING : CONFIRMED;
        request.setStatus(event.getParticipantLimit() == 0 ? CONFIRMED : status);
        ParticipationRequest savedRequest = requestRepository.save(request);
        log.info("Запрос создан: {}", savedRequest);
        return requestMapper.toParticipationRequestDto(savedRequest);
    }

    @Override
    public List<ParticipationRequestDto> get(Long userId) throws UserNotFound {
        log.debug("get({})", userId);
        User user = userValidation(userId);
        List<ParticipationRequestDto> requests = requestRepository.findAllByRequester(user)
                .stream().map(requestMapper::toParticipationRequestDto).collect(Collectors.toList());
        log.info("По запросу пользователя возвращён список заявок: {}", requests);
        return requests;
    }

    @Override
    public ParticipationRequestDto cancel(Long userId, Long requestId) throws RequestNotFound, RequestNotExists, UserNotFound {
        log.debug("cancel({}, {})", userId, requestId);
        User user = userValidation(userId);
        ParticipationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFound("-", "-"));
        if (!request.getRequester().equals(user)) {
            throw new RequestNotExists("-", "-");
        }
        request.setStatus(REJECTED);
        ParticipationRequest savedRequest = requestRepository.save(request);
        log.info("Заявка на участие в событии отменена: {}", savedRequest);
        return requestMapper.toParticipationRequestDto(savedRequest);
    }

    private User userValidation(Long userId) throws UserNotFound {
        log.debug("userValidation({})", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFound("-","-"));
        log.info("Зарос на поиск пользователя прошёл успешно: {}", user);
        return user;
    }
}