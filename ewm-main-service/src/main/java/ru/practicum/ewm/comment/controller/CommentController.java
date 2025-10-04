package ru.practicum.ewm.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CommentShort;
import ru.practicum.ewm.comment.dto.FullCommentDto;
import ru.practicum.ewm.comment.service.CommentService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService service;

    @PostMapping("/users/{userId}/comments")
    public FullCommentDto create(@PathVariable Long userId,
                                 @Valid @RequestBody CommentDto newCommentDto) {
        return service.addCommentToEventByUser(userId, newCommentDto);
    }

    @PatchMapping("/users/{userId}/comments/{commentId}")
    public FullCommentDto update(@PathVariable Long userId,
                                 @PathVariable Long commentId,
                                 @Valid @RequestBody CommentDto updatedCommentDto) {
        return service.updateCommentByUser(userId, commentId, updatedCommentDto);
    }

    @DeleteMapping("/users/{userId}/comments/{commentId}")
    public void deleteOwnComment(@PathVariable Long userId,
                                 @PathVariable Long commentId) {
        service.deleteOwnComment(userId, commentId);
    }

    @DeleteMapping("/admin/comments/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        service.deleteCommentById(commentId);
    }

    @GetMapping("/admin/comments/{eventId}")
    public List<CommentShort> getCommentsForEvent(@PathVariable Long eventId) {
        return service.getCommentsForEvent(eventId);
    }
}