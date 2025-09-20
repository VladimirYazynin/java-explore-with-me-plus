package ru.practicum.ewm.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.service.UserService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    @ResponseStatus(CREATED)
    public UserDto create(@Valid @RequestBody UserDto user) {
        return service.create(user);
    }

    @GetMapping
    public List<UserDto> get(@RequestParam(required = false) Long[] ids,
                             @RequestParam(defaultValue = "0") int from,
                             @RequestParam(defaultValue = "10") int size) {
        return service.get(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long userId) {
        service.delete(userId);
    }
}
