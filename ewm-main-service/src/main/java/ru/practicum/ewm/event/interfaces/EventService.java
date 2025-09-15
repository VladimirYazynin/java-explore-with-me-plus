package ru.practicum.ewm.event.interfaces;

import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.exceptions.EventNotFound;

public interface EventService {
    EventDto getEventById(long id) throws EventNotFound;
}