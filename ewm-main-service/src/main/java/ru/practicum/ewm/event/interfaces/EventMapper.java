package ru.practicum.ewm.event.interfaces;

import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;

public interface EventMapper {
    Event mapToEvent(NewEventDto newEventDto, Category category);

    EventFullDto mapToEventFullDto(Event event, long confirmedRequests, long views);

    EventShortDto mapToEventShortDto(Event event, long views);
}