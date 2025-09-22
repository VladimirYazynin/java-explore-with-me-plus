package ru.practicum.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestInfo {
    private String remoteAddress;
    private String requestURI;
}