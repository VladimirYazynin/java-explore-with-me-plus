package ru.practicum.ewm.exception.model;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class NotFoundException extends EwmException {
    public NotFoundException(String message, String reason) {
        super(message, reason, LocalDateTime.now(), HttpStatus.NOT_FOUND);
    }
}