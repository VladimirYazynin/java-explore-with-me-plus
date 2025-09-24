package ru.practicum.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RequestInfo {
    private String remoteAddress;
    private String requestURI;

    public String[] getUris(List<Long> eventId) {
        StringBuilder sb = new StringBuilder(requestURI);
        return eventId.stream().map(id -> sb.append("/").append(id).toString()).toArray(String[]::new);
    }

    public String getUri(Long id) {
        StringBuilder sb = new StringBuilder(requestURI);
        return sb.append(id).toString();
    }
}