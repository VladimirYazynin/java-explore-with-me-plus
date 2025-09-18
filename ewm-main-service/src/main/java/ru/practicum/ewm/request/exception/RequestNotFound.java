package ru.practicum.ewm.request.exception;

import ru.practicum.ewm.exception.model.NotFoundException;

public class RequestNotFound extends NotFoundException {
    public RequestNotFound(String message, String reason) {
        super(message, reason);
    }
}
