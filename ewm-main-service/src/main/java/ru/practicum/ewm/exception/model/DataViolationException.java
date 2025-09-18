package ru.practicum.ewm.exception.model;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class DataViolationException extends EwmException {
    public DataViolationException(String message, String reason, LocalDateTime timestamp) {
        super(message, reason, timestamp, HttpStatus.BAD_REQUEST);
    }
}