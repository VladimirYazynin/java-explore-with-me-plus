package ru.practicum.ewm.event.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.EndpointHitDto;
import ru.practicum.ewm.ViewStatsDto;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.enums.Sort;
import ru.practicum.ewm.enums.State;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.QEvent;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.repository.LocationRepository;
import ru.practicum.ewm.exception.AccessException;
import ru.practicum.ewm.exception.DataViolationException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.stats.StatService;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static ru.practicum.ewm.enums.Sort.EVENT_DATE;
import static ru.practicum.ewm.enums.Sort.VIEWS;
import static ru.practicum.ewm.enums.State.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final CategoryRepository categoryRepository;
    private final StatService statsService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public EventFullDto create(Long userId, NewEventDto event) {
        log.debug("create({}, {})", userId, event);
        User user = userSearch(userId);
        Event thisEvent = eventMapper.toEvent(event);
        dateValidation(LocalDateTime.parse(event.getEventDate(), formatter));
        thisEvent.setPaid(event.getPaid() != null ? event.getPaid() : false);
        thisEvent.setParticipantLimit(event.getParticipantLimit() != null ? event.getParticipantLimit() : 0);
        thisEvent.setRequestModeration(event.getRequestModeration() != null ? event.getRequestModeration() : true);
        thisEvent.setCreatedOn(LocalDateTime.now());
        thisEvent.setInitiator(user);
        thisEvent.setState(PENDING);
        thisEvent.setLocation(locationRepository.save(event.getLocation()));
        thisEvent.setViews(0);
        thisEvent.setConfirmedRequests(0);
        Event savedEvent = eventRepository.save(thisEvent);
        log.info("Событие сохранено: {}", savedEvent);
        return eventMapper.toEventFullDto(savedEvent);
    }

    @Override
    public List<EventFullDto> getAllByOwner(Long userId, int from, int size) {
        log.debug("getAllByOwner({}, {}, {})", userId, from, size);
        User user = userSearch(userId);
        PageRequest page = PageRequest.of(from, size);
        List<EventFullDto> events = eventRepository.findAllByInitiator(user, page)
                .stream().map(eventMapper::toEventFullDto).collect(Collectors.toList());
        log.info("Возвращён список событий по запросу пользователя: {}", events);
        return events;
    }

    @Override
    public EventFullDto getByIdByOwner(Long userId, Long eventId) {
        log.debug("getByIdByOwner({}, {})", userId, eventId);
        userSearch(userId);
        Event event = eventSearch(eventId);
        log.info("Возвращено событие по запросу пользователя: {}", event);
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto updateByIdByOwner(Long userId, Long eventId, UpdateEventUserRequest event) {
        log.debug("updateByIdByOwner({}, {}, {})", userId, eventId, event);
        User user = userSearch(userId);
        Event thisEvent = eventSearch(eventId);
        if (thisEvent.getState().equals(PUBLISHED)) {
            throw new DataViolationException("Нельзя изменить данные");
        }
        if (event.getEventDate() != null) {
            dateValidation(LocalDateTime.parse(event.getEventDate(), formatter));
        }
        if (!thisEvent.getInitiator().equals(user)) {
            throw new AccessException("Нет доступа");
        }
        Event eventToSave = fieldsChecker(event, thisEvent);
        if (event.getStateAction() != null) {
            switch (State.valueOf(event.getStateAction())) {
                case PUBLISH_EVENT:
                    thisEvent.setPublishedOn(LocalDateTime.now());
                    thisEvent.setState(PUBLISHED);
                    break;
                case REJECT_EVENT:
                case CANCEL_REVIEW:
                    thisEvent.setPublishedOn(LocalDateTime.now());
                    thisEvent.setState(CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    thisEvent.setPublishedOn(LocalDateTime.now());
                    thisEvent.setState(PENDING);
                    break;
            }
        }
        Event updatedEvent = eventRepository.save(eventToSave);
        log.info("Событие обновлено пользователем: {}", updatedEvent);
        return eventMapper.toEventFullDto(updatedEvent);
    }

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId, Long eventId) {
        log.debug("getRequests({}, {})", userId, eventId);
        User user = userSearch(userId);
        Event event = eventSearch(eventId);
        if (!event.getInitiator().equals(user)) {
            throw new AccessException("Нет доступа");
        }
        List<ParticipationRequestDto> requests = requestRepository.findAllByEvent(event)
                .stream().map(requestMapper::toParticipationRequestDto).collect(Collectors.toList());
        log.info("Возвращён список запросов по запросу пользователя: {}", requests);
        return requests;
    }

    @Override
    public EventRequestStatusUpdateResult updateStatus(Long userId, Long eventId,
                                                       EventRequestStatusUpdateRequest request) {
        log.debug("updateStatus({}, {}, {})", userId, eventId, request);
        userSearch(userId);
        Event event = eventSearch(eventId);
        List<ParticipationRequest> requests = requestRepository.findAllByEvent(event);
        List<Long> processingRequests = request.getRequestIds();
        if (request.getStatus().equals(CONFIRMED)) {
            long confirmed = requests.stream().filter(req -> req.getStatus().equals(CONFIRMED)).count();
            if ((confirmed + processingRequests.size()) > event.getParticipantLimit()
                    || !event.getRequestModeration()) {
                throw new DataViolationException("Превышен лимит заявок");
            }
        }
        long confirmedRequests = requests.stream()
                .filter(req -> processingRequests.contains(req.getId()) && req.getStatus().equals(CONFIRMED)).count();
        if (confirmedRequests >= 1) {
            throw new DataViolationException("Нельзя изменить статус у уже подтверждённой заявки");
        }
        List<ParticipationRequestDto> participationRequests = requests.stream()
                .filter(req -> request.getRequestIds().contains(req.getId()))
                .peek(r -> r.setStatus(request.getStatus())).peek(requestRepository::save)
                .map(requestMapper::toParticipationRequestDto).collect(Collectors.toList());
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        if (request.getStatus().equals(REJECTED)) {
            result.setRejectedRequests(participationRequests);
        } else {
            result.setConfirmedRequests(participationRequests);
        }
        log.info("Статус запроса обновлён: {}", result);
        return result;
    }

    @Override
    public List<EventFullDto> searchEventsByAdmin(Long[] users, String[] states, Integer[] categories,
                                                  LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        log.debug("searchEventsByAdmin({}, {}, {}, {}, {}, {}, {})",
                users, states, categories, rangeStart, rangeEnd, from, size);
        PageRequest page = PageRequest.of(from, size, ASC, "id");
        BooleanExpression byUserId;
        BooleanExpression byStateId;
        BooleanExpression byCategoryId;
        BooleanExpression byDate;
        BooleanBuilder builder = new BooleanBuilder();
        if (Objects.nonNull(users)) {
            byUserId = QEvent.event.initiator.id.in(users);
            builder.and(byUserId);
        }
        if (Objects.nonNull(states)) {
            List<State> stateList = Arrays.stream(states).map(State::valueOf).collect(Collectors.toList());
            byStateId = QEvent.event.state.in(stateList);
            builder.and(byStateId);
        }
        if (Objects.nonNull(rangeStart) || Objects.nonNull(rangeEnd)) {
            byDate = QEvent.event.eventDate.between(rangeStart, rangeEnd);
            builder.and(byDate);
        }
        if (Objects.nonNull(categories)) {
            byCategoryId = QEvent.event.category.id.in(categories);
            builder.and(byCategoryId);
        }
        List<EventFullDto> events = eventRepository.findAll(builder, page).getContent().stream()
                .map(eventMapper::toEventFullDto)
                .peek(event -> event.setConfirmedRequests((int) requestRepository.findByEventId(event.getId())
                        .stream().filter(request -> request.getStatus().equals(CONFIRMED))
                        .count())).collect(Collectors.toList());
        log.info("Возвращён список событий по запросу администратора: {}", events);
        return events;
    }

    @Override
    public List<EventShortDto> searchEvents(String text, Integer[] categories, Boolean paid, LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd, Boolean onlyAvailable, Sort sort,
                                            int from, int size, HttpServletRequest request) {
        log.debug("searchEvents({}, {}, {}, {}, {}, {}, {}, {}, {})",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        PageRequest page = PageRequest.of(from / size, size);

        BooleanBuilder builder = new BooleanBuilder();

        // Только опубликованные события
        builder.and(QEvent.event.state.eq(PUBLISHED));

        if (Objects.nonNull(text) && !text.isBlank()) {
            BooleanExpression byAnnotation = QEvent.event.annotation.containsIgnoreCase(text);
            BooleanExpression byDescription = QEvent.event.description.containsIgnoreCase(text);
            builder.and(byAnnotation.or(byDescription));
        }

        if (Objects.nonNull(paid)) {
            builder.and(QEvent.event.paid.eq(paid));
        }

        if (Objects.nonNull(categories) && categories.length > 0) {
            // Передаем массив ID напрямую
            BooleanExpression byCategories = QEvent.event.category.id.in(categories);
            builder.and(byCategories);
        }

        // Логика дат
        LocalDateTime startDate = Objects.nonNull(rangeStart) ? rangeStart : LocalDateTime.now();
        LocalDateTime endDate = Objects.nonNull(rangeEnd) ? rangeEnd : LocalDateTime.now().plusYears(1);

        if (endDate.isBefore(startDate)) {
            throw new ValidationException("Конечная дата не может быть раньше начальной");
        }

        builder.and(QEvent.event.eventDate.between(startDate, endDate));

        // Получаем события
        List<Event> events = eventRepository.findAll(builder, page).getContent();

        // Отправка статистики
        sendData(request);

        // Преобразование в DTO
        List<EventShortDto> shortEvents = events.stream()
                .map(event -> {
                    Event eventWithViews = receiveData(event);
                    return eventMapper.toEventShortDto(eventWithViews);
                })
                .collect(Collectors.toList());

        // Сортировка
        if (sort.equals(VIEWS)) {
            shortEvents.sort(Comparator.comparing(EventShortDto::getViews).reversed());
        } else if (sort.equals(EVENT_DATE)) {
            shortEvents.sort(Comparator.comparing(EventShortDto::getEventDate).reversed());
        }
        log.info("Возвращён список событий: {}", shortEvents.size());
        return shortEvents;
    }

    @Override
    public EventFullDto getById(Long eventId, HttpServletRequest request) {
        log.debug("getById({})", eventId);
        Event event = eventSearch(eventId);
        if (event.getState() != PUBLISHED) {
            throw new NotFoundException("Не найдено");
        }
        sendData(request);
        Event savedEvent = receiveData(event);
        log.info("Возвращено событие по запросу пользователя: {}", savedEvent);
        return eventMapper.toEventFullDto(savedEvent);
    }

    @Override
    public EventFullDto updateByIdByAdmin(Long eventId, UpdateEventAdminRequest event) {
        log.debug("updateByIdByAdmin({}, {})", eventId, event);
        Event thisEvent = eventSearch(eventId);
        if (!thisEvent.getState().equals(PENDING)) {
            throw new DataViolationException("Нельзя изменить данные события");
        }
        if (LocalDateTime.now().isAfter(thisEvent.getEventDate().minusHours(1))) {
            throw new ValidationException("Дата начала изменяемого события должна быть не ранее " +
                    "чем за час от даты публикации");
        }
        if (event.getEventDate() != null &&
                LocalDateTime.parse(event.getEventDate(), formatter).isBefore(LocalDateTime.now())) {
            throw new ValidationException("Дата не может быть в прошлом");
        }
        Event updatedEvent = fieldsChecker(eventMapper.toUpdateEventUserRequest(event), thisEvent);
        if (event.getStateAction() != null) {
            updatedEvent.setState(event.getStateAction().equals(PUBLISH_EVENT.toString()) ? PUBLISHED : CANCELED);
        }
        updatedEvent.setPublishedOn(LocalDateTime.now());
        Event savedEvent = eventRepository.save(updatedEvent);
        log.info("Событие обновлено администратором: {}", savedEvent);
        return eventMapper.toEventFullDto(savedEvent);
    }

    private User userSearch(Long userId) {
        log.debug("userSearch({})", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        log.info("Запрос на поиск пользователя прошёл успешно: {}", user);
        return user;
    }

    private Event eventSearch(Long eventId) {
        log.debug("eventSearch({})", eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
        log.info("Запрос на поиск события прошёл успешно: {}", event);
        return event;
    }

    private Event fieldsChecker(UpdateEventUserRequest eventToUpdate, Event foundEvent) {
        log.debug("fieldsChecker({}, {})", eventToUpdate, foundEvent);
        if (eventToUpdate.getAnnotation() != null) {
            foundEvent.setAnnotation(eventToUpdate.getAnnotation());
        }
        if (eventToUpdate.getCategory() != null) {
            foundEvent.setCategory(categoryRepository.findById(eventToUpdate.getCategory())
                    .orElseThrow(() -> new NotFoundException("Категория не найдена")));
        }
        if (eventToUpdate.getDescription() != null) {
            foundEvent.setDescription(eventToUpdate.getDescription());
        }
        if (eventToUpdate.getLocation() != null) {
            foundEvent.setLocation(locationRepository.save(eventToUpdate.getLocation()));
        }
        if (eventToUpdate.getPaid() != null) {
            foundEvent.setPaid(eventToUpdate.getPaid());
        }
        if (eventToUpdate.getParticipantLimit() != null) {
            foundEvent.setParticipantLimit(eventToUpdate.getParticipantLimit());
        }
        if (eventToUpdate.getRequestModeration() != null) {
            foundEvent.setRequestModeration(eventToUpdate.getRequestModeration());
        }
        if (eventToUpdate.getTitle() != null) {
            foundEvent.setTitle(eventToUpdate.getTitle());
        }
        log.info("Валидация полей прошла успешно: {}", foundEvent);
        return foundEvent;
    }

    private void dateValidation(LocalDateTime date) {
        log.debug("dateValidation({})", date);
        if (date.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Событие должно быть не меньше, чем за 2 часа до текущего времени");
        }
        log.info("Валидация даты прошла успешно: {}", date);
    }

    private void sendData(HttpServletRequest request) {
        log.debug("sendData({})", request);
        EndpointHitDto endpointHit = new EndpointHitDto();
        endpointHit.setApp("event");
        endpointHit.setIp(request.getRemoteAddr());
        endpointHit.setUri(request.getRequestURI());
        endpointHit.setTimestamp(LocalDateTime.now().format(formatter));
        EndpointHitDto saved = statsService.post(endpointHit);
        log.info("Информация по запросу на эндпоинт '{}' успешно отправлена: {}", request.getRequestURI(), saved);
    }

    private Event receiveData(Event event) {
        log.debug("receiveData({})", event);
        List<ViewStatsDto> viewStatsDto = statsService.get(LocalDateTime.now().minusYears(1),
                LocalDateTime.now().plusDays(1), new String[]{"/events/" + event.getId()}, String.valueOf(true));
        event.setViews(viewStatsDto.size());
        Event savedEvent = eventRepository.save(event);
        log.info("Сохранено событие с подсчётом просмотров: {}", savedEvent);
        return savedEvent;
    }
}
