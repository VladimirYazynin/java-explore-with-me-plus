package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(UserDto user);

    List<UserDto> get(Long[] ids, int from, int size);

    void delete(Long userId);
}
