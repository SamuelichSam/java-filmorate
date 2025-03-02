package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.model.user.UserDto;
import ru.yandex.practicum.filmorate.model.user.UserMapper;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDbStorageImpl extends BaseDbStorage<User> implements UserStorage {
    private static final String INSERT_QUERY = "INSERT INTO users(email, login, name, birthday) " +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET " +
            "email = ?, login = ?, name = ?, birthday = ? " +
            "WHERE id = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT id, email, login, name, birthday " +
            "FROM users WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT id, email, login, name, birthday " +
            "FROM users";
    private static final String INSERT_FRIEND_QUERY = "INSERT INTO friendships(user_id, friend_id) " +
            "VALUES (?, ?)";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friendships " +
            "WHERE user_id = ? AND friend_id = ?";
    private static final String FIND_FRIENDS_QUERY = "SELECT u.* " +
            "FROM users u " +
            "JOIN friendships f ON u.id = f.friend_id " +
            "WHERE f.user_id = ?";
    private static final String FIND_COMMON_QUERY = "SELECT u.* " +
            "FROM users u " +
            "JOIN friendships f1 ON u.id = f1.friend_id " +
            "JOIN friendships f2 ON u.id = f2.friend_id " +
            "WHERE f1.user_id = ? AND f2.user_id = ?";

    public UserDbStorageImpl(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public UserDto create(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        long id = insert(
                INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        return UserMapper.toDto(user);
    }

    @Override
    public UserDto update(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return UserMapper.toDto(user);
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id)
                .map(UserMapper::toDto);
    }

    @Override
    public List<UserDto> getAll() {
        return findMany(FIND_ALL_QUERY)
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        update(INSERT_FRIEND_QUERY, id, friendId);
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        delete(DELETE_FRIEND_QUERY, id, friendId);
    }

    @Override
    public List<UserDto> getFriends(Long id) {
        return findMany(FIND_FRIENDS_QUERY, id)
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    public List<UserDto> getCommonFriends(Long id, Long friendId) {
        return findMany(FIND_COMMON_QUERY, id, friendId)
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }
}
