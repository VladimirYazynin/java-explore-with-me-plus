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

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    public EndpointHitDto send(EndpointHitDto endpointHit) {
        EndpointHit endpoint = statsRepository.save(EndpointHitMapper.toEndpointHit(endpointHit));
        return EndpointHitMapper.toEndpointHitDto(endpoint);
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
                if (start == null && end == null) {
                    views = statsRepository.findAllByUriIn(uris);
                } else {
                    views = statsRepository.getByUris(uris, start, end);
                }
            } else {
                views = statsRepository.getByStartAndEnd(start, end);
            }
        }
        return views.stream()
                .map(ViewStatsMapper::toViewStatsDto)
                .toList();
    }
}
