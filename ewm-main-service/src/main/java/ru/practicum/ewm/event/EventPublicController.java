package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.exceptions.EventNotFound;
import ru.practicum.ewm.event.interfaces.EventService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class EventPublicController {

    private final EventService eventService;

    @GetMapping
    List<EventShortDto> getAllPublishedEvents(
            @RequestParam String text,
            @RequestParam Integer[] categories,
            @RequestParam Boolean paid,
            @RequestParam String rangeStart,
            @RequestParam String rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam String sort,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        EventFilter filter =
                new EventFilter(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return eventService.findPublishedEvents(filter);
    }

    @GetMapping("/{id}")
    EventFullDto getEventById(@PathVariable long id) throws EventNotFound {
        return eventService.getEventById(id);
    }
}