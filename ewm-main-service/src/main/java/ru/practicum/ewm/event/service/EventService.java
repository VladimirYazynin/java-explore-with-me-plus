package ru.practicum.ewm.event.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.ewm.enums.Sort;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto create(Long userId, NewEventDto event);

    List<EventFullDto> getAllByOwner(Long userId, int from, int size);

    EventFullDto getByIdByOwner(Long userId, Long eventId);

    EventFullDto updateByIdByOwner(Long userId, Long eventId, UpdateEventUserRequest event);

    List<ParticipationRequestDto> getRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest request);

    List<EventShortDto> searchEvents(String text, Integer[] categories, Boolean paid, LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd, Boolean onlyAvailable, Sort sort, int from, int size,
                                     HttpServletRequest request);

    EventFullDto getById(Long eventId, HttpServletRequest request);

    EventFullDto updateByIdByAdmin(Long eventId, UpdateEventAdminRequest event);

    List<EventFullDto> searchEventsByAdmin(Long[] users, String[] states, Integer[] categories,
                                           LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);
}
