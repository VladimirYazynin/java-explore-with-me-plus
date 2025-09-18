package ru.practicum.ewm.exception;

import ru.practicum.ewm.exception.model.EwmException;

import java.time.LocalDateTime;

public class ApiError {
    private final String status;
    private final String message;
    private final String reason;
    private final LocalDateTime timestamp;

    public ApiError(EwmException exception){
        this.message = exception.getMessage();
        this.reason = exception.getReason();
        this.status = exception.getStatus().toString();
        this.timestamp = exception.getTimestamp();
    }
}