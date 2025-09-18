package ru.practicum.user.service;

import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(UserDto user);

    List<UserDto> get(Long[] ids, int from, int size);

    void delete(Long userId);
}
