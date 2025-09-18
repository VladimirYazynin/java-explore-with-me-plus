package ru.practicum.ewm.exception.model;

import java.time.LocalDateTime;

public class NotExistsException extends EwmException {

    public NotExistsException(String message, String reason, ExceptionStatus status, LocalDateTime timestamp) {
        super(message, reason, status, timestamp);
    }
}