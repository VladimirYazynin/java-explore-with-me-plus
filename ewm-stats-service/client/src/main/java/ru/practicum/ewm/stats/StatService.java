package ru.practicum.ewm.stats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.EndpointHitDto;
import ru.practicum.ewm.ViewStatsDto;
import ru.practicum.ewm.client.BaseClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StatService extends BaseClient {
    private static final String API_PREFIX = "/";

    @Autowired
    public StatService(@Value("${stats-service.url}") String serverUrl,
                       RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public EndpointHitDto post(@RequestBody EndpointHitDto endpoint) {
        ResponseEntity<EndpointHitDto> responseEntity = this.post("/hit", endpoint, new EndpointHitDto());
        return responseEntity.getBody();
    }

    public List<ViewStatsDto> get(LocalDateTime start, LocalDateTime end, String[] uris, String unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> map = Map.of(
                "start", start.format(formatter),
                "end", end.format(formatter),
                "uris", uris != null ? uris : new String[0], // защита от null
                "unique", unique
        );

        // Используем безопасное приведение типов
        ResponseEntity<Object> responseEntity = this.get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", map);

        // Безопасное извлечение тела ответа
        if (responseEntity.getBody() instanceof List) {
            return (List<ViewStatsDto>) responseEntity.getBody();
        } else {
            // Если пришел не List, возвращаем пустой список
            return new ArrayList<>();
        }
    }
}