package ru.practicum.ewm.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CommentShort;
import ru.practicum.ewm.comment.dto.FullCommentDto;
import ru.practicum.ewm.comment.mapper.CommentMapper;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.repository.CommentRepository;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.AccessException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentMapper mapper;

    @Override
    @Transactional
    public FullCommentDto addCommentToEventByUser(Long authorId, CommentDto newCommentDto) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        if (!eventRepository.existsById(newCommentDto.getEventId())) {
            throw new NotFoundException("Событие не найдено");
        }
        Comment newComment = new Comment();
        newComment.setEventId(newCommentDto.getEventId());
        newComment.setAuthor(author);
        newComment.setText(newCommentDto.getText());
        newComment.setCreated(LocalDateTime.now());
        return mapper.toFullCommentDto(commentRepository.save(newComment));
    }

    @Override
    @Transactional
    public FullCommentDto updateCommentByUser(Long authorId, Long commentId, CommentDto updatedCommentDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий не найден"));
        if (!comment.getAuthor().getId().equals(authorId)) {
            throw new AccessException("Не доступно для редактирования");
        }
        comment.setEventId(updatedCommentDto.getEventId());
        comment.setText(updatedCommentDto.getText());
        return mapper.toFullCommentDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteOwnComment(Long userId, Long commentId) {
        if (commentRepository.existsByIdAndAuthorId(commentId, userId)) {
            commentRepository.deleteById(commentId);
        } else {
            throw new NotFoundException("Комментарий не найден");
        }
    }

    @Override
    @Transactional
    public void deleteCommentById(Long commentId) {
        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
        } else {
            throw new NotFoundException("Комментарий не найден");
        }
    }

    public List<CommentShort> getCommentsForEvent(Long eventId) {
        return commentRepository.getCommentsByEventId(eventId);
    }
}