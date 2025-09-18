package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exception.DataViolationException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public UserDto create(UserDto user) {
        log.debug("create({})", user);
        User thisUser = mapper.toUser(user);
        if (repository.existsByName(thisUser.getName())) {
            throw new DataViolationException("Пользователь уже существует");
        }
        User savedUser = repository.save(thisUser);
        log.info("Пользователь сохранён: {}", savedUser);
        return mapper.toUserDto(savedUser);
    }

    @Override
    public List<UserDto> get(Long[] ids, int from, int size) {
        log.debug("get({}, {}, {})", ids, from, size);
        PageRequest page = PageRequest.of(from, size);
        List<UserDto> users;
        if (ids != null) {
            users = repository.findAllByIdIn(List.of(ids), page).stream().map(mapper::toUserDto)
                    .collect(Collectors.toList());
        } else {
            users = repository.findAll(page).stream().map(mapper::toUserDto).collect(Collectors.toList());
        }
        log.info("Возвращён список пользователей по запросу администратора: {}", users);
        return users;
    }

    @Override
    public void delete(Long userId) {
        log.debug("delete({})", userId);
        User thisUser = repository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        repository.delete(thisUser);
        log.info("Администратором удалён пользователь: {}", thisUser);
    }
}
