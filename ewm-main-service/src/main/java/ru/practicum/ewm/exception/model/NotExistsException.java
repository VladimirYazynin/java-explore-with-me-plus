package ru.practicum.ewm.exception.model;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class NotExistsException extends EwmException {
    public NotExistsException(String message, String reason) {
        super(message, reason, LocalDateTime.now(), HttpStatus.CONFLICT);
    }
}