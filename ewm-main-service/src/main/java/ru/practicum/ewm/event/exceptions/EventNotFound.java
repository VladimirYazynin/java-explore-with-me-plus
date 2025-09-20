package ru.practicum.ewm.event.exceptions;

import ru.practicum.ewm.exception.model.ExceptionStatus;
import ru.practicum.ewm.exception.model.NotFoundException;

import java.time.LocalDateTime;

public class EventNotFound extends NotFoundException {

    public EventNotFound(String message, String reason, ExceptionStatus status, LocalDateTime timestamp) {
        super(message, reason, status, timestamp);
    }
}