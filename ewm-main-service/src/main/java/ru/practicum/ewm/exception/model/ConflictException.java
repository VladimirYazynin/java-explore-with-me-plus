package ru.practicum.ewm.exception.model;

import java.time.LocalDateTime;

public class ConflictException extends EwmException {

    public ConflictException(String message, String reason, ExceptionStatus status, LocalDateTime timestamp) {
        super(message, reason, status, timestamp);
    }
}
