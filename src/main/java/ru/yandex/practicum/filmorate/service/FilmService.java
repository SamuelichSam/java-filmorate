package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.FilmDto;

import java.util.List;

public interface FilmService {
    List<FilmDto> getAll();

    FilmDto create(Film film);

    FilmDto update(Film film);

    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    List<FilmDto> getPopularFilms(int count);

    FilmDto getFilmById(Long id);
}
