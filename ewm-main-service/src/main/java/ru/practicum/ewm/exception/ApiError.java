package ru.practicum.ewm.exception;

import ru.practicum.ewm.exception.model.EwmException;
import ru.practicum.ewm.exception.model.ExceptionStatus;

import java.time.LocalDateTime;

public class ApiError {
    private final String message;
    private final String reason;
    private final ExceptionStatus status;
    private final LocalDateTime timestamp;

    public ApiError(EwmException exception){
        this.message = exception.getMessage();
        this.reason = exception.getReason();
        this.status = exception.getStatus();
        this.timestamp = exception.getTimestamp();
    }
}