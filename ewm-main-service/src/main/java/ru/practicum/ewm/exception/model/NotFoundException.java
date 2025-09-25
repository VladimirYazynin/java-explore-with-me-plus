package ru.practicum.ewm.exception.model;

import java.time.LocalDateTime;

public class NotFoundException extends EwmException {

    public NotFoundException(String message, String reason) {
        super(message, reason, ExceptionStatus.NOT_FOUND, LocalDateTime.now());
    }
}