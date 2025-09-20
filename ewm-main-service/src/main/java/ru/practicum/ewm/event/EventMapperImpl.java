package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryMapper;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.interfaces.EventMapper;
import ru.practicum.ewm.user.dto.UserShortDto;
import ru.practicum.ewm.user.mapper.UserMapper;


@Component
@RequiredArgsConstructor
public class EventMapperImpl implements EventMapper {

    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;


    @Override
    public Event mapToEvent(NewEventDto newEvent, Category category) {
        return Event.builder()
                .annotation(newEvent.getAnnotation())
                .category(category)
                .description(newEvent.getDescription())
                .eventDate(newEvent.getEventDate())
                .location(newEvent.getLocation())
                .paid(newEvent.isPaid())
                .participantLimit(newEvent.getParticipantLimit())
                .requestModeration(newEvent.isRequestModeration())
                .title(newEvent.getTitle())
                .build();
    }

    public EventFullDto mapToEventFullDto(Event event, long confirmedRequests, long views) {
        CategoryDto category = null;
        UserShortDto initiator = null;

        if (event.getCategory() != null) {
            category = categoryMapper.mapToCategoryDto(event.getCategory());
        }
        if (event.getInitiator() != null) {
            initiator = userMapper.toUserShortDto(event.getInitiator());
        }

        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(category)
                .confirmedRequests(confirmedRequests)
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(initiator)
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(views)
                .build();
    }
}
