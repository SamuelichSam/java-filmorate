package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import ru.yandex.practicum.filmorate.model.genre.GenreDto;
import ru.yandex.practicum.filmorate.model.genre.GenreMapper;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.*;

@Repository
public class GenreDbStorageImpl extends BaseDbStorage<Genre> implements GenreStorage {
    private static final String FIND_ALL_QUERY = "SELECT * " +
            "FROM genres";
    private static final String FIND_BY_ID_QUERY = "SELECT * " +
            "FROM genres " +
            "WHERE id = ?";
    private static final String CHECK_GENRE_QUERY = "SELECT EXISTS " +
            "(SELECT id " +
            "FROM genres " +
            "WHERE id = ?)";
    private static final String FIND_BY_FILM_ID_QUERY = "SELECT g.* " +
            "FROM genres g " +
            "JOIN film_genres fg ON g.id = fg.genre_id " +
            "JOIN films f ON fg.film_id = f.id " +
            "WHERE f.id = ?";

    public GenreDbStorageImpl(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<GenreDto> getAll() {
        return findMany(FIND_ALL_QUERY)
                .stream()
                .map(GenreMapper::toDto)
                .toList();
    }

    @Override
    public Optional<GenreDto> getGenreById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id)
                .map(GenreMapper::toDto);
    }

    @Override
    public Boolean checkGenre(Long id) {
        return jdbc.queryForObject(CHECK_GENRE_QUERY, Boolean.class, id);
    }

    @Override
    public List<GenreDto> findGenresByFilmId(Long id) {
        return findMany(FIND_BY_FILM_ID_QUERY, id)
                .stream().map(GenreMapper::toDto)
                .toList();
    }
}
