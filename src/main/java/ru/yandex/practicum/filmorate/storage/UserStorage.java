package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.user.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<UserDto> getAll();

    UserDto create(UserDto user);

    UserDto update(UserDto user);

    Optional<UserDto> findById(Long id);

    void addFriend(Long id, Long friendId);

    void deleteFriend(Long id, Long friendId);

    List<UserDto> getFriends(Long id);

    List<UserDto> getCommonFriends(Long id, Long friendId);
}
