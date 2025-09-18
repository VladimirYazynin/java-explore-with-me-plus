package ru.practicum.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

@Component
public class UserMapper {

    public User toUser(UserDto user) {
        return new User(
                user.getId(),
                user.getEmail(),
                user.getName());
    }

    public UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getName());
    }

    public UserShortDto toUserShortDto(User user) {
        return new UserShortDto(
                user.getId(),
                user.getName());
    }
}
