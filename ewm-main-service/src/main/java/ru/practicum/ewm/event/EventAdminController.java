package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFilter;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.event.exceptions.EventConditionException;
import ru.practicum.ewm.event.exceptions.EventNotFound;
import ru.practicum.ewm.event.interfaces.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
public class EventAdminController {

    private final EventService eventService;

    @GetMapping
    List<EventFullDto> getAllEvents(
            @RequestParam Integer[] users,
            @RequestParam String[] states,
            @RequestParam Integer[] categories,
            @RequestParam LocalDateTime rangeStart,
            @RequestParam LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        EventFilter filter = new EventFilter();
        filter.setUsers(users);
        filter.setStates(states);
        filter.setCategories(categories);
        filter.setRangeStart(rangeStart);
        filter.setRangeEnd(rangeEnd);
        filter.setFrom(from);
        filter.setSize(size);
        return eventService.findAllEvents(filter);
    }

    @PatchMapping("/{eventId}")
    EventFullDto patchEvent(
            @PathVariable Long eventId,
            @RequestBody UpdateEventAdminRequest dto
    ) throws EventNotFound, EventConditionException {
        return eventService.updateEvent(eventId, dto);
    }
}