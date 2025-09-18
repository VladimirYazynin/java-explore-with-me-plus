package ru.practicum.ewm.event.exceptions;

import ru.practicum.ewm.exception.model.NotFoundException;

public class EventNotFound extends NotFoundException {
    public EventNotFound(String message, String reason) {
        super(message, reason);
    }
}