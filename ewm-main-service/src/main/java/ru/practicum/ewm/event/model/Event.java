package ru.practicum.ewm.event.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.enums.State;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@Table(name = "events")
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
    private Boolean paid;
    @Column(name = "participant_limit")
    private Integer participantLimit;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    private String title;
    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User initiator;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Enumerated(STRING)
    private State state;
    private Integer views;

    public Event(Long id,
                 String annotation,
                 Category category,
                 String description,
                 LocalDateTime eventDate,
                 Location location,
                 Boolean paid,
                 Integer participantLimit,
                 Boolean requestModeration,
                 String title) {
        this.id = id;
        this.annotation = annotation;
        this.category = category;
        this.description = description;
        this.eventDate = eventDate;
        this.location = location;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.requestModeration = requestModeration;
        this.title = title;
    }
}
