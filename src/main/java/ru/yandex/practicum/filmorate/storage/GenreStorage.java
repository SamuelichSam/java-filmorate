package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.genre.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    List<Genre> getAll();

    Optional<Genre> getGenreById(Long id);

    List<Genre> findGenresByFilmId(Long id);

    Boolean checkGenre(Long id);

}
