package ru.practicum.ewm.event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.exceptions.EventConditionException;
import ru.practicum.ewm.event.exceptions.EventNotFound;
import ru.practicum.ewm.event.exceptions.EventParticipantNotExists;
import ru.practicum.ewm.event.interfaces.EventService;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class EventPrivateController {

    private final EventService eventService;

    @GetMapping("/{userId}/events")
    List<EventShortDto> getEventsAddedByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        EventFilter filter = new EventFilter();
        filter.setFrom(from);
        filter.setSize(size);
        return eventService.findEventsAddedByUser(userId, filter);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(CREATED)
    EventFullDto postNewEvent(@PathVariable Long userId,
                              @RequestBody @Valid NewEventDto newEventDto
    ) throws EventConditionException {
        return eventService.addNewEvent(userId, newEventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    EventFullDto getEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) throws EventNotFound {
        return eventService.findEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    EventFullDto patchEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody UpdateEventUserRequest updateEventUserRequest
    ) throws EventNotFound, EventConditionException {
        return eventService.updateEvent(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    List<ParticipationRequestDto> getEventInfo(
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        return eventService.findParticipationRequestInfo(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    EventRequestStatusUpdateResult patchParticipationRequest(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest
    ) throws EventNotFound, EventParticipantNotExists {
        return eventService.updateParticipationRequestInfo(userId, eventId, eventRequestStatusUpdateRequest);
    }
}