package ru.practicum.service;

import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    EndpointHitDto send(EndpointHitDto endpointHit);

    List<ViewStatsDto> receive(LocalDateTime start, LocalDateTime end, String[] uris, Boolean isUnique);
}
