package ru.practicum.ewm.service;

import ru.practicum.ewm.EndpointHitDto;
import ru.practicum.ewm.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    EndpointHitDto send(EndpointHitDto endpointHit);

    List<ViewStatsDto> receive(LocalDateTime start, LocalDateTime end, String[] uris, Boolean isUnique);
}
