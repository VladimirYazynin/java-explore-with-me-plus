package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.user.model.User;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {
    boolean existsByEventIdAndRequester(Long id, User requester);

    List<ParticipationRequest> findAllByRequester(User requester);

    List<ParticipationRequest> findByEventId(Long eventId);
}
