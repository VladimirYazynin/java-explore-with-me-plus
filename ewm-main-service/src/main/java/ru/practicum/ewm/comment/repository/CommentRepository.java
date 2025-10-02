package ru.practicum.ewm.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.comment.dto.CommentShort;
import ru.practicum.ewm.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    boolean existsByIdAndAuthorId(Long commentId, Long userId);

    List<CommentShort> getCommentsByEventId(Long eventId);
}