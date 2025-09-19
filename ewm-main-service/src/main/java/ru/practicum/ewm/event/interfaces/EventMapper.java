package ru.practicum.ewm.event.interfaces;

import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.dto.NewEventDto;

public interface EventMapper {
    Event mapToEvent(NewEventDto newEventDto);

}
