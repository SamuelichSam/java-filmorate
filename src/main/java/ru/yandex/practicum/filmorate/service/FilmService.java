package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film create(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата релиза фильма раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film addLike(Long filmId, Long userId) {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользваотель не найден"));
        Film film = filmStorage.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));
        user.addLike(filmId);
        film.addLike();
        filmStorage.updateLikes(film);
        userStorage.updateLikes(user);
        log.info("Фильму c id = {} поставлен лайк пользователем с id = {}", filmId, userId);
        return film;
    }

    public Film deleteLike(Long filmId, Long userId) {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользваотель не найден"));
        Film film = filmStorage.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));
        if (user.getLikedFilms().contains(filmId)) {
            user.removeLike(filmId);
            film.removeLike();
            filmStorage.updateLikes(film);
            userStorage.updateLikes(user);
            log.info("Пользователем с id = {} был удален лайк фильму c id = {}", userId, filmId);
        } else {
            throw new ValidationException("Этому фильму пользвователь не ставил лайк");
        }
        return film;
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> popFilms = filmStorage
                .getAll()
                .stream()
                .sorted((film1, film2) -> Long.compare(film2.getLike(), film1.getLike()))
                .limit(count)
                .toList();
        log.info("Получение списока популярных фильмов начиная с 1 и до {}", count);
        return popFilms;
    }
}
