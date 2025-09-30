package ru.practicum.ewm.request.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.ParticipationRequest;

import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class RequestMapper {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public ParticipationRequestDto toParticipationRequestDto(ParticipationRequest request) {
        return new ParticipationRequestDto(
                request.getId(),
                request.getCreated().format(formatter),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getStatus().toString());
    }
}
