package ru.practicum.ewm.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.comment.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}