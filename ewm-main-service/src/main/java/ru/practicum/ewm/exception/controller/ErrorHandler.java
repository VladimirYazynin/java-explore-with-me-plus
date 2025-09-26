package ru.practicum.ewm.exception.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.exception.AccessException;
import ru.practicum.ewm.exception.DataViolationException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.exception.model.ErrorResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ErrorHandler {
    private static final DateTimeFormatter PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException exception) {
        return new ErrorResponse(exception.getMessage(),
                "Запрашиваемые данные не были найдены",
                "NOT_FOUND", LocalDateTime.now().format(PATTERN));
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleAccessException(final AccessException exception) {
        return new ErrorResponse(exception.getMessage(),
                "Нет доступа к запрашиваемому ресурсу",
                "BAD_REQUEST", LocalDateTime.now().format(PATTERN));
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public ErrorResponse handleDataViolationException(final DataViolationException exception) {
        return new ErrorResponse(exception.getMessage(),
                "Нарушение целостности данных",
                "CONFLICT", LocalDateTime.now().format(PATTERN));
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException exception) {
        return new ErrorResponse(exception.getMessage(),
                "Ошибка валидации данных, данные указаны некорректно",
                "BAD_REQUEST", LocalDateTime.now().format(PATTERN));
    }
}
