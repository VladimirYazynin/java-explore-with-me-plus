package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService service;
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    @PostMapping("/hit")
    public EndpointHitDto send(@RequestBody EndpointHitDto endpointHit) {
        return service.send(endpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> receive(@RequestParam @DateTimeFormat(pattern = PATTERN) LocalDateTime start,
                                      @RequestParam @DateTimeFormat(pattern = PATTERN) LocalDateTime end,
                                      @RequestParam(required = false) String[] uris,
                                      @RequestParam(defaultValue = "false") Boolean unique) {
        return service.receive(start, end, uris, unique);
    }
}
