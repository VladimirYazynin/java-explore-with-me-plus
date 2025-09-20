package ru.practicum.ewm.event.dto;

import ru.practicum.ewm.event.Location;
import ru.practicum.ewm.event.StateAction;

import java.time.LocalDateTime;

public class UpdateEventAdminRequest {
    private String annotation;
    private Long category;
    private String description;
    private LocalDateTime eventDate;
    private Location location;
    private boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    private StateAction stateAction;
    private String title;
}