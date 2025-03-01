package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.FilmDto;
import ru.yandex.practicum.filmorate.model.genre.GenreDto;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    List<FilmDto> getAll();

    FilmDto create(FilmDto filmDto);

    FilmDto update(FilmDto filmDto);

    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    List<FilmDto> getPopularFilms(int count);

    Optional<FilmDto> findById(Long id);

    void saveGenres(Long filmId, List<GenreDto> genres);
}
