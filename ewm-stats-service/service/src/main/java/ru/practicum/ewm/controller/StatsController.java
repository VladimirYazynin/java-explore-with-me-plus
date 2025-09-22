package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.EndpointHitDto;
import ru.practicum.ewm.ViewStatsDto;
import ru.practicum.ewm.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService service;
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    @PostMapping("/hit")
    @ResponseStatus(CREATED)
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
