package ru.practicum.ewm.event.exceptions;

import ru.practicum.ewm.exception.model.NotExistsException;

import java.time.LocalDateTime;

public class EventParticipantNotExists extends NotExistsException {
    public EventParticipantNotExists(String message, String reason, LocalDateTime timestamp) {
        super(message, reason, timestamp);
    }
}