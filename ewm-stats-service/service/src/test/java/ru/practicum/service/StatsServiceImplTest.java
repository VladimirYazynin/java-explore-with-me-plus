package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StatsServiceImplTest {
    private final StatsService service;
    private final EndpointHitDto endpoint = new EndpointHitDto(1L, "ewm-main-service", "/events/1",
            "192.163.0.1", "2025-09-06 11:00:23");

    @Test
    void send_shouldSaveEndpointHitData() {
        EndpointHitDto thisEndpoint = service.send(endpoint);

        assertThat(endpoint.getIp(), equalTo(thisEndpoint.getIp()));
        assertThat(endpoint.getApp(), equalTo(thisEndpoint.getApp()));
        assertThat(endpoint.getTimestamp(), equalTo(thisEndpoint.getTimestamp()));
    }

    @Test
    void receive_shouldReturnListOfUniqueIpsWithUri() {
        service.send(new EndpointHitDto(2L, "ewm-main-service", "/events/1",
                "192.163.0.1", "2025-09-06 11:00:23"));
        service.send(new EndpointHitDto(3L, "ewm-main-service", "/events/1",
                "192.163.0.1", "2025-09-06 11:10:00"));
        service.send(new EndpointHitDto(4L, "ewm-main-service", "/events/1",
                "192.163.1.2", "2025-09-06 11:20:00"));
        List<ViewStatsDto> views = service.receive(
                LocalDateTime.of(2025, 9, 6, 10, 0, 23),
                LocalDateTime.of(2025, 9, 6, 12, 0, 23),
                new String[]{"/events/1"}, true);

        assertFalse(views.isEmpty());
        assertEquals(1, views.size());
    }

    @Test
    void receive_shouldReturnListOfIpsWithUris() {
        service.send(new EndpointHitDto(2L, "ewm-main-service", "/events/1",
                "192.163.0.1", "2025-09-06 11:00:23"));
        service.send(new EndpointHitDto(3L, "ewm-main-service", "/events/2",
                "192.163.1.2", "2025-09-06 11:10:00"));
        service.send(new EndpointHitDto(4L, "ewm-main-service", "/events/1",
                "192.163.0.1", "2025-09-06 11:20:00"));
        List<ViewStatsDto> views = service.receive(
                LocalDateTime.of(2025, 9, 6, 10, 0, 23),
                LocalDateTime.of(2025, 9, 6, 12, 0, 23),
                new String[]{"/events/1", "/events/2"}, true);

        assertFalse(views.isEmpty());
        assertEquals(2, views.size());
    }
}