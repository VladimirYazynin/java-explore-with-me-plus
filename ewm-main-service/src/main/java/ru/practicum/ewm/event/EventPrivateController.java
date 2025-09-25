package ru.practicum.ewm.event;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.event.dto.EventFilter;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.event.exceptions.EventNotFound;
import ru.practicum.ewm.event.exceptions.EventStateException;
import ru.practicum.ewm.event.interfaces.EventService;
import ru.practicum.ewm.exception.model.ConflictException;
import ru.practicum.ewm.exception.model.NotExistsException;
import ru.practicum.ewm.exception.model.NotFoundException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.statistics.RequestInfo;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

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
    ) throws EventStateException, NotFoundException {
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
            @RequestBody UpdateEventUserRequest updateEventUserRequest,
            HttpServletRequest request
    ) throws EventNotFound, EventStateException, NotFoundException, NotExistsException, ConflictException {
        RequestInfo info = new RequestInfo(request.getRemoteAddr(), request.getRequestURI());
        return eventService.updateEvent(userId, eventId, updateEventUserRequest, info);
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
    ) throws NotFoundException, NotExistsException, ConflictException {
        return eventService.updateParticipationRequestInfo(userId, eventId, eventRequestStatusUpdateRequest);
    }
}