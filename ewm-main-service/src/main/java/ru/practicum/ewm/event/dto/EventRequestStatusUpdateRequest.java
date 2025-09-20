package ru.practicum.ewm.event.dto;

import ru.practicum.ewm.event.Status;

import java.util.List;

public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private Status status;
}