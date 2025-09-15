package ru.practicum.ewm.event;

import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.exceptions.EventNotFound;
import ru.practicum.ewm.event.interfaces.EventService;

public class EventServiceImpl implements EventService {
    @Override
    public EventDto getEventById(long id) throws EventNotFound {
        return null;
    }
}
