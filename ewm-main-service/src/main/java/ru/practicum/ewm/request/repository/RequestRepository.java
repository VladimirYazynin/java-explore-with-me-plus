package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.user.model.User;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {
    boolean existsByEventAndRequester(Event event, User requester);

    List<ParticipationRequest> findAllByRequester(User requester);

    List<ParticipationRequest> findAllByEvent(Event event);

    List<ParticipationRequest> findByEventId(Long eventId);
}
