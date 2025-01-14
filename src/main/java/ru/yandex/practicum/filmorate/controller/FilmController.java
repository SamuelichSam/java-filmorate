package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Добавление фильма");

        filmValidation(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Добавлен фильм {}", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("Обновление фильма {}", film);
        if (film.getId() == null) {
            log.error("Не указан id фильма");
            throw new ValidationException("Не указан id фильма");
        }
        filmValidation(film);
        Film exFilm = films.get(film.getId());
        if (films.containsKey(film.getId())) {
            if (exFilm.getName() != null) {
                exFilm.setName(film.getName());
            }
            if (exFilm.getDescription() != null) {
                exFilm.setDescription(film.getDescription());
            }
            if (exFilm.getReleaseDate() != null) {
                exFilm.setReleaseDate(film.getReleaseDate());
            }
            if (exFilm.getDuration() != null) {
                exFilm.setDuration(film.getDuration());
            }
            films.put(film.getId(), exFilm);
            log.info("Обновлен фильм {} с id - {}", film, film.getId());
            return exFilm;
        }
        log.error("Ошибка при обновлении фильма");
        throw new NotFoundException("Фильм с id - " + film.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void filmValidation(Film film) {
        if (film.getName() == null) {
            log.error("Название фильма пустое");
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.error("Описание фильма больше 200 символов");
            throw new ValidationException("Описание фильма не должно превышать 200 символов");
        }
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1985, 12, 28))) {
            log.error("Дата релиза фильма раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            log.error("Продолжительность фильма отрицательное число");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }
}
