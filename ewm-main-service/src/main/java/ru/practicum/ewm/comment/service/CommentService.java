package ru.practicum.ewm.comment.service;

import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CommentShort;
import ru.practicum.ewm.comment.dto.FullCommentDto;

import java.util.List;


public interface CommentService {
    FullCommentDto addCommentToEventByUser(Long authorId, CommentDto newCommentDto);

    FullCommentDto updateCommentByUser(Long authorId, Long commentId, CommentDto updatedCommentDto);

    void deleteOwnComment(Long userId, Long commentId);

    void deleteCommentById(Long commentId);

    List<CommentShort> getCommentsForEvent(Long eventId);
}