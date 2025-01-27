package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserStorage userStorage;

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public User addFriend(Long id, Long friendId) {
        User user = userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользватель не найден"));
        User friend = userStorage.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользватель не найден"));
        if (user.getFriends().contains(friendId)) {
            log.error("Повторное добавление в друзья");
            throw new ValidationException("Этот пользователь уже является другом");
        }
        user.getFriends().add(friendId);
        log.info("В список друзей пользователя с id = {} добавлен пользователь с id = {}", id, friendId);
        friend.getFriends().add(id);
        userStorage.updateFriends(user);
        userStorage.updateFriends(friend);
        return user;
    }

    public User deleteFriend(Long id, Long friendId) {
        User user = userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользватель не найден"));
        User friend = userStorage.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользватель не найден"));
        user.getFriends().remove(friendId);
        log.info("Из списка друзей пользователя с id = {} удален пользователь с id = {}", id, friendId);
        friend.getFriends().remove(id);
        log.info("Из списка друзей пользователя с id = {} удален пользователь с id = {}", friendId, id);
        userStorage.updateFriends(user);
        userStorage.updateFriends(friend);
        return user;
    }

    public Collection<User> getFriends(Long id) {
        Set<User> friends = new HashSet<>();
        for (Long friendId : userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользватель не найден")).getFriends()) {
            User friend = userStorage.findById(friendId)
                    .orElseThrow(() -> new NotFoundException("Пользватель не найден"));
            friends.add(friend);
            log.info("Получение списка друзей пользователя с id = {}", id);
        }
        return friends;
    }

    public Collection<User> getCommonFriends(Long id, Long friendId) {
        User user = userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользватель не найден"));
        User friend = userStorage.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользватель не найден"));
        Set<Long> commonFriends = user.getFriends();
        Set<Long> friends = friend.getFriends();
        commonFriends.retainAll(friends);
        log.info("Получение общего списка друзей пользователя с id = {} и ползователя с id = {}", id, friendId);
        return commonFriends.stream()
                .map(userId -> userStorage.findById(userId)
                        .orElseThrow(() -> new NotFoundException("Пользватель не найден")))
                .collect(Collectors.toList());
    }
}
