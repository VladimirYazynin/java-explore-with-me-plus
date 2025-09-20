package ru.practicum.ewm.exception.model;

import java.time.LocalDateTime;

public class NotPublishedException extends EwmException {

    public NotPublishedException(String message, String reason, ExceptionStatus status, LocalDateTime timestamp) {
        super(message, reason, status, timestamp);
    }

}
