package ru.practicum.ewm.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.exceptions.EventNotFound;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.exception.RequestNotExists;
import ru.practicum.ewm.request.exception.RequestNotFound;
import ru.practicum.ewm.request.service.RequestService;
import ru.practicum.ewm.user.exception.UserNotFound;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(CREATED)
   ParticipationRequestDto create(@PathVariable Long userId, @RequestParam Long eventId)
            throws EventNotFound, RequestNotExists, UserNotFound {
        return requestService.create(userId, eventId);
    }

    @GetMapping("/users/{userId}/requests")
    List<ParticipationRequestDto> get(@PathVariable Long userId) throws UserNotFound {
        return requestService.get(userId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    ParticipationRequestDto cancel(@PathVariable Long userId, @PathVariable Long requestId)
            throws UserNotFound, RequestNotExists, RequestNotFound {
        return requestService.cancel(userId, requestId);
    }
}