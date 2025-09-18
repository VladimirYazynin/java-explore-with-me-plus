package ru.practicum.ewm.event.dto;

import ru.practicum.ewm.event.Location;

import java.time.LocalDateTime;

public class NewEventDto {
    private String annotation;
    private Long category;
    private String description;
    private LocalDateTime eventDate;
    private Location location;
    private boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    private String title;
}