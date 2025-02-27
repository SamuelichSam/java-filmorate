package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.model.user.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAll();

    UserDto create(User user);

    UserDto update(User user);

    void addFriend(Long id, Long friendId);

    void deleteFriend(Long id, Long friendId);

    List<UserDto> getFriends(Long id);

    List<UserDto> getCommonFriends(Long id, Long otherId);
}
