package ru.practicum.ewm.event;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFilter;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.statistics.RequestInfo;
import ru.practicum.ewm.event.exceptions.EventNotFound;
import ru.practicum.ewm.event.interfaces.EventService;

import java.time.LocalDateTime;
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
            @RequestParam LocalDateTime rangeStart,
            @RequestParam LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam String sort,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request
    ) {
        EventFilter filter = new EventFilter();
        filter.setText(text);
        filter.setCategories(categories);
        filter.setPaid(paid);
        filter.setRangeStart(rangeStart);
        filter.setRangeEnd(rangeEnd);
        filter.setOnlyAvailable(onlyAvailable);
        filter.setSort(sort);
        filter.setFrom(from);
        filter.setSize(size);
        RequestInfo info = new RequestInfo(request.getRemoteAddr(), request.getRequestURI());
        return eventService.findPublishedEvents(filter, info);
    }

    @GetMapping("/{id}")
    EventFullDto getEventById(@PathVariable long id, HttpServletRequest request) throws EventNotFound {
        RequestInfo info = new RequestInfo(request.getRemoteAddr(), request.getRequestURI());
        return eventService.getPublishedEventById(id, info);
    }
}