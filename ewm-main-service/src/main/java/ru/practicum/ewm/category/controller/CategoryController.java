package ru.practicum.ewm.category.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.service.CategoryService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService service;

    @PostMapping("/admin/categories")
    @ResponseStatus(CREATED)
    public CategoryDto create(@Valid @RequestBody CategoryDto category) {
        return service.create(category);
    }

    @PatchMapping("/admin/categories/{catId}")
    public CategoryDto update(@PathVariable Integer catId,
                              @Valid @RequestBody CategoryDto category) {
        return service.update(catId, category);
    }

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Integer catId) {
        service.delete(catId);
    }

    @GetMapping("/categories")
    public List<CategoryDto> get(@RequestParam(defaultValue = "0") int from,
                                 @RequestParam(defaultValue = "10") int size) {
        return service.get(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getById(@PathVariable Integer catId) {
        return service.getById(catId);
    }
}
