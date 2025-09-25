package ru.practicum.ewm.exception.model;

import java.time.LocalDateTime;

public class NotExistsException extends EwmException {

    public NotExistsException(String message, String reason) {
        super(message, reason, ExceptionStatus.CONFLICT, LocalDateTime.now());
    }
}