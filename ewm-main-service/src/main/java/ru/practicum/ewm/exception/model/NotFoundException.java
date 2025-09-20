package ru.practicum.ewm.exception.model;

import java.time.LocalDateTime;

public class NotFoundException extends EwmException {

    public NotFoundException(String message, String reason, ExceptionStatus status, LocalDateTime timestamp) {
        super(message, reason, status, timestamp);
    }
}