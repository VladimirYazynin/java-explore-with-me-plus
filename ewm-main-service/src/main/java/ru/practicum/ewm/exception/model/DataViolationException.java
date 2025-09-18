package ru.practicum.ewm.exception.model;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class DataViolationException extends EwmException {
    public DataViolationException(String message, String reason) {
        super(message, reason, LocalDateTime.now(), HttpStatus.BAD_REQUEST);
    }
}