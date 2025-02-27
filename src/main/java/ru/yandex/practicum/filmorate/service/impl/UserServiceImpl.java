package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.model.user.UserDto;
import ru.yandex.practicum.filmorate.model.user.UserMapper;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public List<UserDto> getAll() {
        log.info("Получение всех пользователей");
        return userStorage.getAll()
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    public UserDto create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.info("Создание пользователя {}", user);
        return UserMapper.toDto(userStorage.create(user));
    }

    @Override
    public UserDto update(User user) {
        if (user.getId() == null) {
            throw new ValidationException("Не указан id");
        }
        checkId(user.getId());
        log.info("Обновление пользователя {}", user);
        return UserMapper.toDto(userStorage.update(user));
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        checkId(id);
        checkId(friendId);
        userStorage.addFriend(id, friendId);
        log.info("В список друзей пользователя с id = {} добавлен пользователь с id = {}", id, friendId);
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        checkId(id);
        checkId(friendId);
        userStorage.deleteFriend(id, friendId);
        log.info("Из списка друзей пользователя с id = {} удален пользователь с id = {}", friendId, id);
    }

    @Override
    public List<UserDto> getFriends(Long id) {
        checkId(id);
        log.info("Получение списка друзей пользователя с id = {}", id);
        return userStorage.getFriends(id)
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    public List<UserDto> getCommonFriends(Long id, Long otherId) {
        log.info("Получение общего списка друзей пользователя с id = {} и ползователя с id = {}", id, otherId);
        return userStorage.getCommonFriends(id, otherId)
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }

    public void checkId(Long id) {
        userStorage.findById(id)
                .map(UserMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }
}
