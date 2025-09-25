package ru.practicum.ewm.exception.model;

import java.time.LocalDateTime;

public class DataViolationException extends EwmException {

    public DataViolationException(String message, String reason) {
        super(message, reason, ExceptionStatus.BAD_REQUEST, LocalDateTime.now());
    }
}