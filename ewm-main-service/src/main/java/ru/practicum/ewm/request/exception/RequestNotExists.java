package ru.practicum.ewm.request.exception;

import ru.practicum.ewm.exception.model.NotExistsException;

public class RequestNotExists extends NotExistsException {
    public RequestNotExists(String message, String reason) {
        super(message, reason);
    }
}
