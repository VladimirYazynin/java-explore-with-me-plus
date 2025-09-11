package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.mapper.ViewStatsMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private final EndpointHitMapper endpointHitMapper;
    private final ViewStatsMapper viewStatsMapper;

    @Override
    public EndpointHitDto send(EndpointHitDto endpointHit) {
        EndpointHit endpoint = statsRepository.save(endpointHitMapper.toEndpointHit(endpointHit));
        return endpointHitMapper.toEndpointHitDto(endpoint);
    }

    @Override
    public List<ViewStatsDto> receive(LocalDateTime start, LocalDateTime end, String[] uris, Boolean isUnique) {
        List<ViewStats> views;
        if (isUnique) {
            if (uris != null) {
                views = statsRepository.getDistinctByUris(uris, start, end);
            } else {
                views = statsRepository.getDistinctByStartAndEnd(start, end);
            }
        } else {
            if (uris != null) {
                views = statsRepository.getByUris(uris, start, end);
            } else {
                views = statsRepository.getByStartAndEnd(start, end);
            }
        }
        return views.stream().map(viewStatsMapper::toViewStatsDto).collect(Collectors.toList());
    }
}
