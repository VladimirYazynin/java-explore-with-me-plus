package ru.practicum.ewm.stats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class StatService extends BaseClient {
    private static final String API_PREFIX = "/";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
        try {
            String startStr = start.format(FORMATTER);
            String endStr = end.format(FORMATTER);

            StringBuilder urlBuilder = new StringBuilder("/stats?start={start}&end={end}");

            Map<String, Object> parameters = new java.util.HashMap<>();
            parameters.put("start", startStr);
            parameters.put("end", endStr);

            if (uris != null && uris.length > 0) {
                urlBuilder.append("&uris={uris}");
                parameters.put("uris", String.join(",", uris));
            }

            if (unique != null) {
                urlBuilder.append("&unique={unique}");
                parameters.put("unique", unique);
            }

            String url = urlBuilder.toString();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<List<ViewStatsDto>> responseEntity = rest.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<>() {
                    },
                    parameters
            );

            List<ViewStatsDto> result = responseEntity.getBody();

            return result != null ? result : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
