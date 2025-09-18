package ru.practicum.ewm.exception.model;

import java.time.LocalDateTime;

public class DataViolationException extends EwmException {

    DataViolationException(String message, String reason, ExceptionStatus status, LocalDateTime timestamp) {
        super(message, reason, status, timestamp);
    }
}