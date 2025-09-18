package ru.practicum.ewm.exception.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public abstract class EwmException extends Exception {

    private final HttpStatus status;
    private final String reason;
    private final LocalDateTime timestamp;

    EwmException(String message, String reason, LocalDateTime timestamp, HttpStatus status) {
        super(message);
        this.status = status;
        this.reason = reason;
        this.timestamp = timestamp;
    }
}