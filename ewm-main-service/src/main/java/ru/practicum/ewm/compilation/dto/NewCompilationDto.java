package ru.practicum.ewm.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    private Long id;
    private List<Long> events;
    private Boolean pinned;
    @NotBlank
    @Length(min = 1, max = 50)
    private String title;
}
