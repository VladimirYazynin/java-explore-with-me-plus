package ru.practicum.ewm.event.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventFilter {
    private String text;
    private String[] states;
    private Integer[] categories;
    private Integer[] users;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable;
    private String sort;
    private int from;
    private int size;
}