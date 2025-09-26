package ru.practicum.ewm.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.ErrorResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class ErrorHandler {
    private static final DateTimeFormatter PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleDateTimeFormatException(final DateTimeFormatException exception) {
        return new ErrorResponse(exception.getMessage(),
                "Ошибка валидации передаваемых даты и времени",
                "BAD_REQUEST", LocalDateTime.now().format(PATTERN));
    }
}
