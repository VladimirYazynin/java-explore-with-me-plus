package ru.practicum.ewm.event;

import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.exceptions.EventNotFound;
import ru.practicum.ewm.event.interfaces.EventService;

import java.util.List;

public class EventServiceImpl implements EventService {
    @Override
    public EventFullDto getEventById(long id) throws EventNotFound {
        return null;
    }

    @Override
    public List<EventShortDto> findPublishedEvents(EventFilter filter) {
        return List.of();
    }
}
