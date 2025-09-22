package ru.practicum.statistics;

import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsClient {
    void post(RequestInfo info);
    List<ViewStatsDto> get(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique);
    ViewStatsDto get(LocalDateTime start, LocalDateTime end, RequestInfo info, boolean unique);
}