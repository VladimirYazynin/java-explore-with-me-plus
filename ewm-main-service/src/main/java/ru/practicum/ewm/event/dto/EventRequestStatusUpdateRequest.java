package ru.practicum.ewm.event.dto;

import lombok.Data;
import ru.practicum.ewm.enums.State;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private State status;
}
