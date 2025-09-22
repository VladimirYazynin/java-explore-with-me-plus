package ru.practicum.ewm.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.EndpointHitDto;
import ru.practicum.ewm.event.StatResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class StatClient extends BaseClient {

    private static final String API_PREFIX = "/";

    @Autowired
    public StatClient(@Value("${stats-service.url}") String serverUrl, RestTemplateBuilder builder) {
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

    public List<StatResponse> getEventStat(String[] uris) {
        Map<String, Object> map = Map.of(
                "uris", uris
        );
        ResponseEntity<ArrayList<StatResponse>> responseEntity =
                this.get("/stats?uris={uris}", map, new ArrayList<>());
        return responseEntity.getBody();
    }

}
