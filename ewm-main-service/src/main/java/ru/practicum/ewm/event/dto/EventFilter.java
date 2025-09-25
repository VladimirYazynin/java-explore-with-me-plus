package ru.practicum.ewm.event.dto;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.Data;
import ru.practicum.ewm.event.State;

import java.time.LocalDateTime;
import java.util.Objects;

import static ru.practicum.ewm.event.QEvent.*;

@Data
public class EventFilter {
    private String text;
    private State[] states;
    private Long[] categories;
    private Long[] users;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable;
    private Sort sort;
    private int from;
    private int size;

    public void addStates(State... state) {
        if (Objects.isNull(states)) {
            this.states = state;
        }else{

        }
    }

    public Predicate getPredicate() {
        return event.annotation.containsIgnoreCase(text)
                .or(event.description.containsIgnoreCase(text))
                .and(event.state.in(states))
                .and(event.initiator.id.in(users))
                .and(event.category.id.in(categories))
                .and(event.paid.eq(paid))
                .and(getByOnlyAvailable())
                .and(getByDateRangeExpression());
    }

    private BooleanExpression getByOnlyAvailable() {
        return onlyAvailable ? event.participationRequests.size().ne(event.participantLimit) : null;
    }

    private BooleanExpression getByDateRangeExpression() {
        if (Objects.isNull(rangeStart) || Objects.isNull(rangeEnd)) {
            return event.eventDate.after(LocalDateTime.now());
        } else {
            return event.eventDate.between(rangeStart, rangeEnd);
        }
    }

    public static enum Sort{
        EVENT_DATE, VIEWS;
    }
}