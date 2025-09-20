package ru.practicum.ewm.exception.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public abstract class EwmException extends Exception {

    private final ExceptionStatus status;
    private final String reason;
    private final LocalDateTime timestamp;

    EwmException(String message, String reason, ExceptionStatus status, LocalDateTime timestamp) {
        super(message);
        this.status = status;
        this.reason = reason;
        this.timestamp = timestamp;
    }
}