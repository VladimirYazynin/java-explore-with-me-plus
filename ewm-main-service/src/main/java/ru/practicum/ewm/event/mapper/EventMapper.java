package ru.practicum.ewm.event.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.mapper.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class EventMapper {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    private final UserMapper userMapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Event toEvent(NewEventDto event) {
        return new Event(
                event.getId(),
                event.getAnnotation(),
                categoryRepository.findById(event.getCategory())
                        .orElseThrow(() -> new NotFoundException("Категория не найдена")),
                event.getDescription(),
                LocalDateTime.parse(event.getEventDate(), formatter),
                event.getLocation(),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getRequestModeration(),
                event.getTitle());
    }

    public EventFullDto toEventFullDto(Event event) {
        String publishedOn = null;
        if (event.getPublishedOn() != null) {
            publishedOn = event.getPublishedOn().format(formatter);
        }
        return new EventFullDto(
                event.getId(),
                event.getAnnotation(),
                categoryMapper.toCategoryDto(event.getCategory()),
                event.getDescription(),
                event.getEventDate().format(formatter),
                event.getLocation(),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getRequestModeration(),
                event.getTitle(),
                event.getConfirmedRequests(),
                event.getCreatedOn().format(formatter),
                userMapper.toUserShortDto(event.getInitiator()),
                publishedOn,
                event.getState(),
                event.getViews());
    }

    public EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(
                event.getId(),
                event.getAnnotation(),
                categoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate().format(formatter),
                userMapper.toUserShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                event.getViews());
    }

    public UpdateEventUserRequest toUpdateEventUserRequest(UpdateEventAdminRequest event) {
        return new UpdateEventUserRequest(
                event.getId(),
                event.getAnnotation(),
                event.getCategory(),
                event.getDescription(),
                event.getEventDate(),
                event.getLocation(),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getRequestModeration(),
                event.getStateAction(),
                event.getTitle());
    }
}
