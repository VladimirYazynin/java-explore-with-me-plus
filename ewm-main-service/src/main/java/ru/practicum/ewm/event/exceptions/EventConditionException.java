package ru.practicum.ewm.event.exceptions;

import ru.practicum.ewm.exception.model.DataViolationException;
import ru.practicum.ewm.exception.model.ExceptionStatus;

import java.time.LocalDateTime;

public class EventConditionException extends DataViolationException {
    public EventConditionException(String message, String reason, ExceptionStatus status, LocalDateTime timestamp) {
        super(message, reason, status, timestamp);
    }
}