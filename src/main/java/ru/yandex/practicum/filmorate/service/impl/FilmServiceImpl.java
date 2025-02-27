
package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.FilmDto;
import ru.yandex.practicum.filmorate.model.film.FilmMapper;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    public FilmServiceImpl(FilmStorage filmStorage, MpaStorage mpaStorage, GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    @Override
    public List<FilmDto> getAll() {
        List<Film> films = filmStorage.getAll();
        for (Film film : films) {
            List<Genre> genres = genreStorage.findGenresByFilmId(film.getId());
            film.setGenres(genres);
        }
        log.info("Получение всех фильмов");
        return films.stream()
                .map(FilmMapper::toDto)
                .toList();
    }

    @Override
    public FilmDto create(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата выхода не может быть раньше " +
                    "28.12.1895 - даты выхода первого в истории фильма");
        }
        if (film.getMpa().getId() != null) {
            if (!mpaStorage.checkMpa(film.getMpa().getId())) {
                throw new NotFoundException("Рейтинг не найден");
            }
        }
        Film createdFilm = filmStorage.create(film);
        if (film.getGenres() != null) {
            List<Genre> genres = film.getGenres()
                    .stream()
                    .distinct()
                    .toList();
            for (Genre g : genres) {
                if (!genreStorage.checkGenre(g.getId())) {
                    throw new NotFoundException("Жанр не найден");
                }
                createdFilm.setGenres(genres);
            }
        }
        filmStorage.saveGenres(createdFilm);
        log.info("Создание фильма {}", film);
        return FilmMapper.toDto(createdFilm);
    }

    @Override
    public FilmDto update(Film film) {
        if (film.getMpa().getId() != null) {
            if (!mpaStorage.checkMpa(film.getMpa().getId())) {
                throw new NotFoundException("Рейтинг не найден");
            }
        }
        Film updatedFilm = filmStorage.update(film);
        if (film.getGenres() != null) {
            List<Genre> genres = film.getGenres();
            for (Genre g : genres) {
                if (!genreStorage.checkGenre(g.getId())) {
                    throw new NotFoundException("Жанр не найден");
                }
            }
        }
        log.info("Обновление фильма {}", film);
        return FilmMapper.toDto(updatedFilm);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        filmStorage.addLike(filmId, userId);
        log.info("Фильму c id = {} поставлен лайк пользователем с id = {}", filmId, userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        filmStorage.deleteLike(filmId, userId);
        log.info("Пользователем с id = {} был удален лайк фильму c id = {}", userId, filmId);

    }

    @Override
    public List<FilmDto> getPopularFilms(int count) {
        List<Film> films = filmStorage.getPopularFilms(count);
        for (Film film : films) {
            List<Genre> genres = genreStorage.findGenresByFilmId(film.getId());
            film.setGenres(genres);
        }
        log.info("Получение списка популярных фильмов начиная с 1 и до {}", count);
        return films.stream()
                .map(FilmMapper::toDto)
                .toList();
    }

    @Override
    public FilmDto getFilmById(Long id) {
        List<Genre> genres = genreStorage.findGenresByFilmId(id);
        Film resultFilm = filmStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));
        resultFilm.setGenres(genres);
        log.info("Получение фильма по id = {}", id);
        return FilmMapper.toDto(resultFilm);
    }
}

