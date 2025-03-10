package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.FilmDto;
import ru.yandex.practicum.filmorate.model.film.FilmMapper;
import ru.yandex.practicum.filmorate.model.genre.GenreDto;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class FilmDbStorageImpl extends BaseDbStorage<Film> implements FilmStorage {
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, release_date, duration, mpa_id) " +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films SET " +
            "name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
            "WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT f.*, m.name AS mpa_name " +
            "FROM films f " +
            "LEFT JOIN mpa m ON f.mpa_id = m.id ";
    private static final String INSERT_FILM_GENRES_QUERY = "INSERT INTO film_genres(film_id, genre_id) " +
            "VALUES (?, ?)";
    private static final String INSERT_LIKE_QUERY = "INSERT INTO film_likes(film_id, user_id) " +
            "VALUES (?, ?)";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM film_likes " +
            "WHERE film_id = ? AND user_id = ?";
    private static final String FIND_POP_QUERY = "SELECT f.id, f.name, f.description, f.release_date, " +
            "f.duration, f.mpa_id, m.name AS mpa_name, COUNT(fl.user_id) AS likes_count " +
            "FROM films f " +
            "LEFT JOIN film_likes fl ON f.id = fl.film_id " +
            "LEFT JOIN mpa m ON f.mpa_id = m.id " +
            "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, f.mpa_id " +
            "ORDER BY likes_count DESC " +
            "LIMIT ?";
    private static final String FIND_BY_ID_QUERY = "SELECT f.id AS film_id, f.name AS film_name, f.description, " +
            "f.release_date, f.duration, m.id AS mpa_id, m.name AS mpa_name, " +
            "STRING_AGG(g.name, ', ') AS genres " +
            "FROM films f " +
            "LEFT JOIN mpa m ON f.mpa_id = m.id " +
            "LEFT JOIN film_genres fg ON f.id = fg.film_id " +
            "LEFT JOIN genres g ON fg.genre_id = g.id " +
            "WHERE f.id = ? " +
            "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, m.id, m.name";

    public FilmDbStorageImpl(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public FilmDto create(FilmDto filmDto) {
        Film film = FilmMapper.toFilm(filmDto);
        Long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);
        return FilmMapper.toDto(film);
    }

    @Override
    public FilmDto update(FilmDto filmDto) {
        Film film = FilmMapper.toFilm(filmDto);
        update(UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        return FilmMapper.toDto(film);
    }

    @Override
    public List<FilmDto> getAll() {
        return findMany(FIND_ALL_QUERY)
                .stream()
                .map(FilmMapper::toDto)
                .toList();
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        update(INSERT_LIKE_QUERY, filmId, userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        delete(DELETE_LIKE_QUERY, filmId, userId);
    }

    @Override
    public List<FilmDto> getPopularFilms(int count) {
        return findMany(FIND_POP_QUERY, count)
                .stream()
                .map(FilmMapper::toDto)
                .toList();
    }

    @Override
    public Optional<FilmDto> findById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id)
                .map(FilmMapper::toDto);
    }

    @Override
    public void saveGenres(Long filmId, List<GenreDto> genres) {
        jdbc.batchUpdate(INSERT_FILM_GENRES_QUERY, genres.stream()
                .map(genre -> new Object[]{filmId, genre.id()})
                .collect(Collectors.toList())
        );
    }
}
