package ru.practicum.ewm.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatResponse {
    private String app;
    private String uri;
    private Integer hits;
}
