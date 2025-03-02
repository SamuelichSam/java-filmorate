package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.genre.GenreDto;

import java.util.List;

public interface GenreService {
    List<GenreDto> getAll();

    GenreDto getGenreById(Long id);
}
