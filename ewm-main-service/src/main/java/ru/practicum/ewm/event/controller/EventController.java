package ru.practicum.ewm.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.enums.Sort;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(CREATED)
    public EventFullDto create(@PathVariable Long userId, @Valid @RequestBody NewEventDto event) {
        return eventService.create(userId, event);
    }

    @GetMapping("/users/{userId}/events")
    public List<EventFullDto> getAllByOwner(@PathVariable Long userId,
                                            @RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "10") int size) {
        return eventService.getAllByOwner(userId, from, size);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getByIdByOwner(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.getByIdByOwner(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto updateByIdByOwner(@PathVariable Long userId,
                                          @PathVariable Long eventId,
                                          @Valid @RequestBody UpdateEventUserRequest event) {
        return eventService.updateByIdByOwner(userId, eventId, event);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsByOwner(@PathVariable Long userId,
                                                            @PathVariable Long eventId) {
        return eventService.getRequests(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStatus(@PathVariable Long userId,
                                                       @PathVariable Long eventId,
                                                       @RequestBody EventRequestStatusUpdateRequest request) {
        return eventService.updateStatus(userId, eventId, request);
    }

    @GetMapping("/events")
    public List<EventShortDto> searchEvents(@RequestParam(required = false) String text,
                                            @RequestParam(required = false) Integer[] categories,
                                            @RequestParam(required = false) Boolean paid,
                                            @RequestParam(required = false)
                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                            @RequestParam(required = false)
                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                            @RequestParam(defaultValue = "true", required = false) Boolean onlyAvailable,
                                            @RequestParam(required = false, defaultValue = "EVENT_DATE") Sort sort,
                                            @RequestParam(required = false, defaultValue = "0") int from,
                                            @RequestParam(required = false, defaultValue = "10") int size,
                                            HttpServletRequest request) {
        return eventService.searchEvents(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/events/{eventId}")
    public EventFullDto getById(@PathVariable Long eventId, HttpServletRequest request) {
        return eventService.getById(eventId, request);
    }

    @GetMapping("/admin/events")
    public List<EventFullDto> search(@RequestParam(required = false) Long[] users,
                                     @RequestParam(required = false) String[] states,
                                     @RequestParam(required = false) Integer[] categories,
                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                          LocalDateTime rangeStart,
                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                          LocalDateTime rangeEnd,
                                     @RequestParam(required = false, defaultValue = "0") int from,
                                     @RequestParam(required = false, defaultValue = "10") int size) {
        return eventService.searchEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventFullDto adminVerification(@PathVariable Long eventId,
                                          @Valid @RequestBody UpdateEventAdminRequest event) {
        return eventService.updateByIdByAdmin(eventId, event);
    }
}
