package ru.practicum.statistics.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatController {
    private final StatService service;
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto post(@RequestBody EndpointHitDto endpoint) {
        return service.post(endpoint);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> receive(@RequestParam @DateTimeFormat(pattern = PATTERN) LocalDateTime start,
                                      @RequestParam @DateTimeFormat(pattern = PATTERN) LocalDateTime end,
                                      @RequestParam(required = false) String[] uris,
                                      @RequestParam(defaultValue = "false") String unique) {
        return service.get(start, end, uris, unique);
    }
}
