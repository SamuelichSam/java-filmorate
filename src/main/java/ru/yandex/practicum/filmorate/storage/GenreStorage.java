package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.genre.GenreDto;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    List<GenreDto> getAll();

    Optional<GenreDto> getGenreById(Long id);

    List<GenreDto> findGenresByFilmId(Long id);

    Boolean checkGenre(Long id);

}
