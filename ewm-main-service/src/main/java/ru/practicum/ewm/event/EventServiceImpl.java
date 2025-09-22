package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.event.dto.EventFilter;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.statistics.RequestInfo;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.event.exceptions.EventStateException;
import ru.practicum.ewm.event.exceptions.EventNotFound;
import ru.practicum.ewm.event.exceptions.EventParticipantNotExists;
import ru.practicum.ewm.event.interfaces.EventMapper;
import ru.practicum.ewm.event.interfaces.EventService;
import ru.practicum.ewm.exception.model.NotFoundException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.statistics.StatsClient;

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
    private final StatsClient statClient;
    private final EventMapper eventMapper;

    @Override
    public EventFullDto getPublishedEventById(long eventId, RequestInfo info) throws EventNotFound {
        Event event = eventRepository.findByIdAndState(eventId, PUBLISHED)
                .orElseThrow(() -> new EventNotFound("Событие не найдено", "id нет в базе или не опубликовано"));
        List<ParticipationRequest> requests = requestRepository.findAllByEventIdAndStatus(eventId, CONFIRMED);
        // запрос в сервис статистики
        long views = statClient.get(event.getPublishedOn(), LocalDateTime.now(), info, false).getHits();
        statClient.post(info);

        return eventMapper.mapToEventFullDto(event, views, requests.size());
    }

    @Override
    public List<EventShortDto> findPublishedEvents(EventFilter filter, RequestInfo info) {



        statClient.post(info);
        return null;
    }

    @Override
    public List<EventFullDto> findAllEvents(EventFilter filter) {
        return null;
    }

    @Override
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest dto, RequestInfo info) throws NotFoundException, EventStateException {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFound("Событие не найдено", "id нет в базе"));
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
                    throw new EventStateException("Не возможно опубликовать событие", "До начала осталось менее 1 часа");
                }
            } else {
                throw new EventStateException("Нельзя опубликовать данное событие", "Событие находится не в состоянии ожидания");
            }
        }
        if (dto.getStateAction() == REJECT_EVENT) {
            if (event.getState() == PUBLISHED) {
                throw new EventStateException("Нельзя отменить данное событие", "Событие уже опубликовано");
            } else {
                event.setState(CANCELED);
            }
        }

        if (dto.getAnnotation() != null) {
            event.setAnnotation(dto.getAnnotation());
        }
        if (dto.getCategory() != null) {
            Category newCategory = categoryRepository.findById(dto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Категория не найдена", "id нет в базе"));
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
        long views = statClient.get(event.getPublishedOn(),LocalDateTime.now(),info,false).getHits();

        eventRepository.save(event);
        return eventMapper.mapToEventFullDto(event, requests.size(), views);
    }

    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest dto,RequestInfo info) throws EventNotFound, EventStateException {
        return null;
    }

    @Override
    public List<EventShortDto> findEventsAddedByUser(Long userId, EventFilter filter) {
        return null;
    }

    @Override
    public EventFullDto addNewEvent(Long userId, NewEventDto newEventDto) throws EventStateException, NotFoundException {
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден", "id нет в базе"));
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Категория не найдена", "id нет в базе"));
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