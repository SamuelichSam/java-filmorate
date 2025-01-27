package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public User create(User entity) {
        log.info("Добавление пользователя");
        entity.setId(getNextId());
        if (entity.getName() == null || entity.getName().isBlank()) {
            entity.setName(entity.getLogin());
            log.warn("Используем логин как имя");
        }
        users.put(entity.getId(), entity);
        log.info("Пользователь добавлен");
        return entity;
    }

    @Override
    public User update(User entity) {
        log.info("Обновление пользователя {}", entity);
        User exUser = users.get(entity.getId());
        if (users.containsKey(entity.getId())) {
            if (entity.getEmail() != null) {
                exUser.setEmail(entity.getEmail());
            }
            if (entity.getLogin() != null) {
                exUser.setLogin(entity.getLogin());
            }
            if (entity.getName() != null) {
                exUser.setName(entity.getName());
            }
            if (entity.getBirthday() != null) {
                exUser.setBirthday(entity.getBirthday());
            }
            users.put(entity.getId(), exUser);
            log.info("Обновлен пользователь {} с id - {}", entity, entity.getId());
            return exUser;
        }
        log.error("Ошибка при обновлении пользвоателя");
        throw new NotFoundException("Пользователь с id - " + entity.getId() + " не найден");
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void updateFriends(User user) {
        if (users.containsKey(user.getId())) {
            User exUser = users.get(user.getId());
            exUser.setFriends(user.getFriends());
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    @Override
    public void updateLikes(User etity) {
        if (users.containsKey(etity.getId())) {
            User exUser = users.get(etity.getId());
            exUser.setLikedFilms(etity.getLikedFilms());
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
