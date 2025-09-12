package ru.practicum.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.controller.StatsController;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StatsController.class)
public class StatsControllerTest {
    @MockBean
    private StatsService service;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    private final EndpointHitDto endpoint = new EndpointHitDto(1L, "ewm-main-service", "/events/1",
            "192.163.0.1", "2025-09-06 11:00:23");

    @Test
    @SneakyThrows
    void send_shouldSendData() {
        when(service.send(any())).thenReturn(endpoint);

        mvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(endpoint))
                        .characterEncoding(UTF_8)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(endpoint.getId()), Long.class))
                .andExpect(jsonPath("$.app", is(endpoint.getApp())))
                .andExpect(jsonPath("$.uri", is(endpoint.getUri())))
                .andExpect(jsonPath("$.ip", is(endpoint.getIp())))
                .andExpect(jsonPath("$.timestamp", is(endpoint.getTimestamp())));
    }

    @Test
    @SneakyThrows
    void shouldReturnListOfData() {
        when(service.receive(any(), any(), any(), anyBoolean()))
                .thenReturn(List.of(new ViewStatsDto("ewm-main-service", "/events/1", 1)));

        mvc.perform(get("/stats?start=2020-05-05 00:00:00&end=2035-05-05 00:00:00")
                        .characterEncoding(UTF_8)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].app", is("ewm-main-service")))
                .andExpect(jsonPath("$.[0].uri", is("/events/1")))
                .andExpect(jsonPath("$.[0].hits", is(1)));
    }
}
