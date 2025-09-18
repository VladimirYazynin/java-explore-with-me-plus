package ru.practicum.ewm.user.exception;

import ru.practicum.ewm.exception.model.NotFoundException;

public class UserNotFound extends NotFoundException {
    public UserNotFound(String message, String reason) {
        super(message, reason);
    }
}
