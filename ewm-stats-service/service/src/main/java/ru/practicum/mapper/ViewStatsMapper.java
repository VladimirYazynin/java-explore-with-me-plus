package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.ViewStats;

@Component
public class ViewStatsMapper {

    public ViewStatsDto toViewStatsDto(ViewStats viewStats) {
        return new ViewStatsDto(
                viewStats.getApp(),
                viewStats.getUri(),
                (int) viewStats.getHits());
    }
}
