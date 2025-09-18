package ru.practicum.ewm.request.service;

import ru.practicum.ewm.event.exceptions.EventNotFound;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.exception.RequestNotExists;
import ru.practicum.ewm.request.exception.RequestNotFound;
import ru.practicum.ewm.user.exception.UserNotFound;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto create(Long userId, Long eventId) throws EventNotFound, RequestNotExists, UserNotFound;

    List<ParticipationRequestDto> get(Long userId) throws UserNotFound;

    ParticipationRequestDto cancel(Long userId, Long requestId) throws RequestNotFound, RequestNotExists, UserNotFound;
}
