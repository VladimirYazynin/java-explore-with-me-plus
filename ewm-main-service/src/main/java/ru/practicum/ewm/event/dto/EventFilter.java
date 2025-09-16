package ru.practicum.ewm.event.dto;

import java.time.LocalDateTime;

public class EventFilter {
    public EventFilter(String text, Integer[] categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, int from, int size) {
    }

    public EventFilter(Integer[] users, String[] states, Integer[] categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
    }

    public EventFilter(int from, int size) {
    }
}