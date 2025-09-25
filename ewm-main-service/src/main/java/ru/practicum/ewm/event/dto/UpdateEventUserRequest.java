package ru.practicum.ewm.event.dto;

import lombok.Data;
import ru.practicum.ewm.event.Location;
import ru.practicum.ewm.event.StateAction;

import java.time.LocalDateTime;

@Data
public class UpdateEventUserRequest {
    private String annotation;
    private Long category;
    private String description;
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
    private String title;
}