package ru.practicum.ewm.event;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFilter;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.event.exceptions.EventStateException;
import ru.practicum.ewm.event.exceptions.EventNotFound;
import ru.practicum.ewm.event.interfaces.EventService;
import ru.practicum.ewm.exception.model.NotFoundException;
import ru.practicum.statistics.RequestInfo;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
public class EventAdminController {

    private final EventService eventService;

    @GetMapping
    List<EventFullDto> getAllEvents(
            @RequestParam Long[] users,
            @RequestParam State[] states,
            @RequestParam Long[] categories,
            @RequestParam LocalDateTime rangeStart,
            @RequestParam LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request
    ) {
        EventFilter filter = new EventFilter();
        filter.setUsers(users);
        filter.setStates(states);
        filter.setCategories(categories);
        filter.setRangeStart(rangeStart);
        filter.setRangeEnd(rangeEnd);
        filter.setFrom(from);
        filter.setSize(size);
        RequestInfo info = new RequestInfo(request.getRemoteAddr(), request.getRequestURI());
        return eventService.findAllEvents(filter, info);
    }

    @PatchMapping("/{eventId}")
    EventFullDto patchEvent(
            @PathVariable Long eventId,
            @RequestBody UpdateEventAdminRequest dto,
            HttpServletRequest request
    ) throws NotFoundException, EventStateException {
        RequestInfo info = new RequestInfo(request.getRemoteAddr(), request.getRequestURI());
        return eventService.updateEvent(eventId, dto, info);
    }
}