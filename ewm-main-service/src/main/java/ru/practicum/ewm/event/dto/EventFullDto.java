package ru.practicum.ewm.event.dto;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.Location;
import ru.practicum.ewm.event.State;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;

public class EventFullDto {
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    private LocalDateTime eventDate;
    private Long id;
    private UserShortDto initiator;
    private Location location;
    private boolean paid;
    private int participantLimit;
    private LocalDateTime publishedOn;
    private boolean requestModeration;
    private State state;
    private String title;
    private long views;
}