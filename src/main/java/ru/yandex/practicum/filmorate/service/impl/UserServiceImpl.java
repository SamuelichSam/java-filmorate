package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.user.UserDto;
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
        return userStorage.getAll();
    }

    @Override
    public UserDto create(UserDto userDto) {
        String name = userDto.name();
        if (userDto.name() == null || userDto.name().isBlank()) {
            name = userDto.login();
        }
        UserDto createdUserDto = new UserDto(
                userDto.id(),
                userDto.email(),
                userDto.login(),
                name, // обновленное имя
                userDto.birthday()
        );
        log.info("Создание пользователя {}", userDto);
        return userStorage.create(userDto);
    }

    @Override
    public UserDto update(UserDto userDto) {
        if (userDto.id() == null) {
            throw new ValidationException("Не указан id");
        }
        checkId(userDto.id());
        log.info("Обновление пользователя {}", userDto);
        return userStorage.update(userDto);
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
        return userStorage.getFriends(id);
    }

    @Override
    public List<UserDto> getCommonFriends(Long id, Long otherId) {
        log.info("Получение общего списка друзей пользователя с id = {} и ползователя с id = {}", id, otherId);
        return userStorage.getCommonFriends(id, otherId);
    }

    public void checkId(Long id) {
        userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
    }
}
