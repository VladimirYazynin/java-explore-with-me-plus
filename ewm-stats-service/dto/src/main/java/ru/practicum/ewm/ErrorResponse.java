package ru.practicum.ewm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private final String message;
    private final String reason;
    private final String status;
    private final String timestamp;
}
