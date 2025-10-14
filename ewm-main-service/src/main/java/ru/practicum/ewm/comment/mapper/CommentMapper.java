package ru.practicum.ewm.comment.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.FullCommentDto;
import ru.practicum.ewm.comment.model.Comment;

@Component
public class CommentMapper {

    public FullCommentDto toFullCommentDto(Comment comment) {
        FullCommentDto commentDto = new FullCommentDto();
        commentDto.setId(comment.getId());
        commentDto.setEventId(comment.getEventId());
        commentDto.setAuthorName(comment.getAuthor().getName());
        commentDto.setText(comment.getText());
        commentDto.setCreated(comment.getCreated());
        return commentDto;
    }

    public CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getEventId(), comment.getText());
    }
}