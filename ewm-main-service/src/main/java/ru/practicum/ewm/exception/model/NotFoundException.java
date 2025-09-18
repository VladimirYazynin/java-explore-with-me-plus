package ru.practicum.ewm.exception.model;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class NotFoundException extends EwmException {
    public NotFoundException(String message, String reason, LocalDateTime timestamp) {
        super(message, reason, timestamp, HttpStatus.NOT_FOUND);
    }
}