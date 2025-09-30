package ru.practicum.ewm.comment.service;

import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.FullCommentDto;


public interface CommentService {
    FullCommentDto addCommentToEventByUser(Long authorId, CommentDto newCommentDto);

}