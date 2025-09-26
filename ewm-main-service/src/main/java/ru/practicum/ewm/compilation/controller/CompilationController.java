package ru.practicum.ewm.compilation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDto;
import ru.practicum.ewm.compilation.service.CompilationService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
public class CompilationController {
    private final CompilationService service;

    @PostMapping("/admin/compilations")
    @ResponseStatus(CREATED)
    public CompilationDto create(@Valid @RequestBody NewCompilationDto compilation) {
        return service.create(compilation);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationDto update(@PathVariable Long compId, @Valid @RequestBody UpdateCompilationDto compilation) {
        return service.update(compId, compilation);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long compId) {
        service.delete(compId);
    }

    @GetMapping("/compilations")
    public List<CompilationDto> getAll(@RequestParam(required = false) Boolean pinned,
                                       @RequestParam(required = false, defaultValue = "0") int from,
                                       @RequestParam(required = false, defaultValue = "10") int size) {
        return service.getAll(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getById(@PathVariable Long compId) {
        return service.getById(compId);
    }
}
