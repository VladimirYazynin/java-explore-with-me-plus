package ru.practicum.ewm.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.FullCommentDto;
import ru.practicum.ewm.comment.service.CommentService;

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
}