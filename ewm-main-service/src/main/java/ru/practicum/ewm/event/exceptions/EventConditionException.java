package ru.practicum.ewm.event.exceptions;

import ru.practicum.ewm.exception.model.DataViolationException;

import java.time.LocalDateTime;

public class EventConditionException extends DataViolationException {
    public EventConditionException(String message, String reason, LocalDateTime timestamp) {
        super(message, reason, timestamp);
    }
}