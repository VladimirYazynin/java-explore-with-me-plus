package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.EndpointHitDto;
import ru.practicum.ewm.ViewStatsDto;
import ru.practicum.ewm.exception.DateTimeFormatException;
import ru.practicum.ewm.mapper.EndpointHitMapper;
import ru.practicum.ewm.mapper.ViewStatsMapper;
import ru.practicum.ewm.model.EndpointHit;
import ru.practicum.ewm.model.ViewStats;
import ru.practicum.ewm.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private final ViewStatsMapper viewStatsMapper;

    @Override
    public EndpointHitDto send(EndpointHitDto endpointHit) {
        log.info("send({})", endpointHit);
        EndpointHit data = EndpointHitMapper.toEndpointHit(endpointHit);
        EndpointHit endpoint = statsRepository.save(data);
        log.info("Сохранена информация по запросу в сервис: {}", endpoint);
        return EndpointHitMapper.toEndpointHitDto(endpoint);
    }

    @Override
    public List<ViewStatsDto> receive(LocalDateTime start, LocalDateTime end, String[] uris, Boolean isUnique) {
        log.info("receive({}, {}, {}, {})", start, end, uris, isUnique);
        if (start.isAfter(end)) {
            throw new DateTimeFormatException("Неверно указаны данные даты и времени");
        }
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
        List<ViewStatsDto> statistics = views.stream()
                .map(viewStatsMapper::toViewStatsDto).collect(Collectors.toList());
        log.info("Возращена статистика по просмотрам: {}", statistics);
        return statistics;
    }
}
