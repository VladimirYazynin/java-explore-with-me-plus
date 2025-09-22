package ru.practicum.ewm.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatRequest {
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
