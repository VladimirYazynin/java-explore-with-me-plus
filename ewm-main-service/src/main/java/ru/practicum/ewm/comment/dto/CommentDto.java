package ru.practicum.ewm.comment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDto {
    @NotNull
    private Long eventId;
    @Size(min = 10, max = 256)
    private String text;
}