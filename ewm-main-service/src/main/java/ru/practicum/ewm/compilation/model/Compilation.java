package ru.practicum.ewm.compilation.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.ewm.event.model.Event;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@Table(name = "compilations")
@AllArgsConstructor
@NoArgsConstructor
public class Compilation {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @ManyToMany
    private List<Event> events = new ArrayList<>();
    private Boolean pinned;
    private String title;
}
