package ru.practicum.ewm.event.interfaces;

import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.exceptions.EventConditionException;
import ru.practicum.ewm.event.exceptions.EventNotFound;
import ru.practicum.ewm.event.exceptions.EventParticipantNotExists;
import ru.practicum.ewm.exception.model.NotFoundException;
import ru.practicum.ewm.exception.model.NotPublishedException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

public interface EventService {
    EventFullDto getEventById(long id, RequestInfo info) throws EventNotFound, NotPublishedException;

    List<EventShortDto> findPublishedEvents(EventFilter filter, RequestInfo info);

    List<EventFullDto> findAllEvents(EventFilter filter);

    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest dto) throws EventNotFound, EventConditionException;

    List<EventShortDto> findEventsAddedByUser(Long userId, EventFilter filter);

    EventFullDto addNewEvent(Long userId, NewEventDto newEventDto) throws EventConditionException, NotFoundException;

    EventFullDto findEvent(
            Long userId,
            Long eventId
    ) throws EventNotFound;

    EventFullDto updateEvent(
            Long userId,
            Long eventId,
            UpdateEventUserRequest dto) throws EventNotFound, EventConditionException;

    List<ParticipationRequestDto> findParticipationRequestInfo(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateParticipationRequestInfo(
            Long userId,
            Long eventId,
            EventRequestStatusUpdateRequest dto) throws EventNotFound, EventParticipantNotExists;
}