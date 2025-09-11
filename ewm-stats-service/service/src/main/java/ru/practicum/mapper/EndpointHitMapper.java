package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.EndpointHitDto;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class EndpointHitMapper {
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return new EndpointHit(
                endpointHitDto.getId(),
                endpointHitDto.getApp(),
                endpointHitDto.getUri(),
                endpointHitDto.getIp(),
                LocalDateTime.parse(endpointHitDto.getTimestamp(), format));
    }

    public EndpointHitDto toEndpointHitDto(EndpointHit endpointHit) {
        return new EndpointHitDto(
                endpointHit.getId(),
                endpointHit.getApp(),
                endpointHit.getUri(),
                endpointHit.getIp(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(endpointHit.getTimestamp()));
    }
}
