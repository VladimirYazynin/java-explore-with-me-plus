package ru.practicum.ewm.exception;

public class DataViolationException extends RuntimeException {
    public DataViolationException(String message) {
        super(message);
    }
}
