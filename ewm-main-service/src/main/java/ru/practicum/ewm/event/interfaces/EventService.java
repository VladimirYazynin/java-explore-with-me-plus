package ru.practicum.ewm.event.interfaces;

import ru.practicum.ewm.event.EventFilter;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.event.exceptions.EventNotFound;

import java.util.List;

public interface EventService {
    EventFullDto getEventById(long id) throws EventNotFound;

    List<EventShortDto> findPublishedEvents(EventFilter filter);

    List<EventFullDto> findAllEvents(EventFilter filter);

    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest dto) throws EventNotFound;
}