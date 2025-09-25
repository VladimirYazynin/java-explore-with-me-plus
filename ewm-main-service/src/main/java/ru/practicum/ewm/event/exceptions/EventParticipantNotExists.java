package ru.practicum.ewm.event.exceptions;

import ru.practicum.ewm.exception.model.NotExistsException;

public class EventParticipantNotExists extends NotExistsException {

    public EventParticipantNotExists(String message, String reason) {
        super(message, reason);
    }
}