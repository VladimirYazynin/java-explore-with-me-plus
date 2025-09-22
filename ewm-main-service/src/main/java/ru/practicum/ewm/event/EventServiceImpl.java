package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.client.StatClient;
import ru.practicum.ewm.event.dto.EventFilter;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.RequestInfo;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.event.exceptions.EventConditionException;
import ru.practicum.ewm.event.exceptions.EventNotFound;
import ru.practicum.ewm.event.exceptions.EventParticipantNotExists;
import ru.practicum.ewm.event.interfaces.EventMapper;
import ru.practicum.ewm.event.interfaces.EventService;
import ru.practicum.ewm.exception.model.ConflictException;
import ru.practicum.ewm.exception.model.ExceptionStatus;
import ru.practicum.ewm.exception.model.NotFoundException;
import ru.practicum.ewm.exception.model.NotPublishedException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.event.State.CANCELED;
import static ru.practicum.ewm.event.State.CONFIRMED;
import static ru.practicum.ewm.event.State.PENDING;
import static ru.practicum.ewm.event.State.PUBLISHED;
import static ru.practicum.ewm.event.StateAction.PUBLISH_EVENT;
import static ru.practicum.ewm.event.StateAction.REJECT_EVENT;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final StatClient statClient;
    private final EventMapper eventMapper;
    @Value("${stats-service.url}")
    private String statServiceUrl;
    @Value("${main-service.name}")
    private String appName;

    @Override
    public EventFullDto getEventById(long eventId, RequestInfo info) throws EventNotFound, NotPublishedException {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFound(
                        "Событие не найдено",
                        "id нет в базе",
                        ExceptionStatus.NOT_FOUND,
                        LocalDateTime.now()
                ));
        if (event.getState() != PUBLISHED) {
            throw new NotPublishedException(
                    "Событие ещё не опубликовано",
                    "Событие проходит модерацию",
                    ExceptionStatus.BAD_REQUEST,
                    LocalDateTime.now()
            );
        }

        List<ParticipationRequest> requests = requestRepository.findAllByEventIdAndStatus(eventId, CONFIRMED);
        // запрос в сервис статистики
        long views = 0;

        return eventMapper.mapToEventFullDto(event, views, requests.size());
    }

    @Override
    public List<EventShortDto> findPublishedEvents(EventFilter filter, RequestInfo info) {
        return null;
    }

    @Override
    public List<EventFullDto> findAllEvents(EventFilter filter) {
        return null;
    }

    @Override
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest dto) throws EventNotFound, EventConditionException, ConflictException, NotFoundException {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFound(
                        "Событие не найдено",
                        "id нет в базе",
                        ExceptionStatus.NOT_FOUND,
                        LocalDateTime.now()
                ));
        if (dto.getEventDate() != null) {
            event.setEventDate(dto.getEventDate());
        }
        if (dto.getStateAction() == PUBLISH_EVENT) {
            if (event.getState() == PENDING) {
                LocalDateTime publishedOn = LocalDateTime.now();
                if (publishedOn.plusHours(1).isBefore(event.getEventDate())) {
                    event.setState(PUBLISHED);
                    event.setPublishedOn(publishedOn);
                } else {
                    throw new ConflictException(
                            "Не возможно опубликовать событие",
                            "До начала события осталось менее 1 часа",
                            ExceptionStatus.CONFLICT,
                            LocalDateTime.now()
                    );
                }
            } else {
                throw new ConflictException(
                        "Нельзя опубликовать данное событие",
                        "Событие находится не в состоянии ожидания",
                        ExceptionStatus.CONFLICT,
                        LocalDateTime.now()
                );
            }
        }
        if (dto.getStateAction() == REJECT_EVENT) {
            if (event.getState() == PUBLISHED) {
                throw new ConflictException(
                        "Нельзя отменить данное событие",
                        "Событие уже опубликовано",
                        ExceptionStatus.CONFLICT,
                        LocalDateTime.now()
                );
            } else {
                event.setState(CANCELED);
            }
        }

        if (dto.getAnnotation() != null) {
            event.setAnnotation(dto.getAnnotation());
        }
        if (dto.getCategory() != null) {
            Category newCategory = categoryRepository.findById(dto.getCategory())
                    .orElseThrow(() -> new NotFoundException(
                            "Категория не найдена",
                            "id нет в базе",
                            ExceptionStatus.NOT_FOUND,
                            LocalDateTime.now()
                    ));
            event.setCategory(newCategory);
        }
        if (dto.getDescription() != null) {
            event.setDescription(dto.getDescription());
        }
        if (dto.getLocation() != null) {
            event.setLocation(dto.getLocation());
        }
        if (dto.getPaid() != null) {
            event.setPaid(dto.getPaid());
        }
        if (dto.getParticipantLimit() != null) {
            event.setParticipantLimit(dto.getParticipantLimit());
        }
        if (dto.getRequestModeration() != null) {
            event.setRequestModeration(dto.getRequestModeration());
        }
        if (dto.getTitle() != null) {
            event.setTitle(dto.getTitle());
        }
        List<ParticipationRequest> requests = requestRepository.findAllByEventIdAndStatus(eventId, CONFIRMED);
        List<StatResponse> views = statClient.getEventStat(new String[]{"/events/" + eventId});

        eventRepository.save(event);
        return eventMapper.mapToEventFullDto(event, requests.size(), views.size());
    }

    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest dto) throws EventNotFound, EventConditionException {
        return null;
    }

    @Override
    public List<EventShortDto> findEventsAddedByUser(Long userId, EventFilter filter) {
        return null;
    }

    @Override
    public EventFullDto addNewEvent(Long userId, NewEventDto newEventDto) throws EventConditionException, NotFoundException {
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        "Пользователь не найден",
                        "id нет в базе",
                        ExceptionStatus.NOT_FOUND,
                        LocalDateTime.now()
                ));
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException(
                        "Категория не найдена",
                        "id нет в базе",
                        ExceptionStatus.NOT_FOUND,
                        LocalDateTime.now()
                ));
        Event newEvent = eventMapper.mapToEvent(newEventDto, category);
        newEvent.setInitiator(initiator);
        newEvent.setState(PENDING);
        newEvent.setCreatedOn(LocalDateTime.now());
        eventRepository.save(newEvent);

        return eventMapper.mapToEventFullDto(newEvent, 0L, 0L);
    }

    @Override
    public EventFullDto findEvent(Long userId, Long eventId) throws EventNotFound {
        return null;
    }

    @Override
    public List<ParticipationRequestDto> findParticipationRequestInfo(Long userId, Long eventId) {
        return null;
    }

    @Override
    public EventRequestStatusUpdateResult updateParticipationRequestInfo(Long userId, Long eventId, EventRequestStatusUpdateRequest dto) throws EventNotFound, EventParticipantNotExists {
        return null;
    }
}