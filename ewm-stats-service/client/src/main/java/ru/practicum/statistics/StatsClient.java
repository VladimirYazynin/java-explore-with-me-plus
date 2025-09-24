package ru.practicum.statistics;

import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface StatsClient {
    void post(RequestInfo info, Set<String> strings);
    List<ViewStatsDto> get(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique);
    ViewStatsDto get(LocalDateTime start, LocalDateTime end, RequestInfo info, boolean unique);

    void post(RequestInfo info);
}