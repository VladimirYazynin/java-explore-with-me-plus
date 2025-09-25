package ru.practicum.ewm.event.exceptions;

import ru.practicum.ewm.exception.model.DataViolationException;

public class EventStateException extends DataViolationException {
    public EventStateException(String message, String reason) {
        super(message, reason);
    }
}