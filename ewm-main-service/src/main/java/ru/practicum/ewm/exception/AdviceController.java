package ru.practicum.ewm.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.exception.model.DataViolationException;
import ru.practicum.ewm.exception.model.NotExistsException;
import ru.practicum.ewm.exception.model.NotFoundException;

@RestControllerAdvice
public class AdviceController {

    //Response Code is 404
    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<ApiError> handleNotFoundException(NotFoundException exception) {
        return new ResponseEntity<>(new ApiError(exception), exception.getStatus());
    }

    //Response Code is 409
    @ExceptionHandler(NotExistsException.class)
    ResponseEntity<ApiError> handleNotExistsException(NotExistsException exception) {
        return new ResponseEntity<>(new ApiError(exception), exception.getStatus());
    }

    //Response Code is 400
    @ExceptionHandler(DataViolationException.class)
    ResponseEntity<ApiError> handleDataViolationException(DataViolationException exception) {
        return new ResponseEntity<>(new ApiError(exception), exception.getStatus());
    }
}