package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Добавление пользователя");
        userValidation(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Пользователь добавлен");
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info("Обновление пользователя {}", user);
        if (user.getId() == null) {
            log.error("Не указан id пользователя");
            throw new ValidationException("Не указан id пользователя");
        }
        userValidation(user);
        User exUser = users.get(user.getId());
        if (users.containsKey(user.getId())) {
            if (user.getEmail() != null) {
                exUser.setEmail(user.getEmail());
            }
            if (user.getLogin() != null) {
                exUser.setLogin(user.getLogin());
            }
            if (user.getName() != null) {
                exUser.setName(user.getName());
            }
            if (user.getBirthday() != null) {
                exUser.setBirthday(user.getBirthday());
            }
            users.put(user.getId(), exUser);
            log.info("Обновлен пользователь {} с id - {}", user, user.getId());
            return exUser;
        }
        log.error("Ошибка при обновлении пользвоателя");
        throw new NotFoundException("Пользователь с id - " + user.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void userValidation(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            log.error("Ошибка в почте");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().contains(" ")) {
            log.error("Логин пустой или с пробелами");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.warn("Используем логин как имя");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Ошибка в дате рождения");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
