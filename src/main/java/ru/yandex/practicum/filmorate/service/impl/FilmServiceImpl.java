
package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.film.FilmDto;
import ru.yandex.practicum.filmorate.model.genre.GenreDto;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private static final LocalDate FILM_DATE = LocalDate.of(1895, 12, 28);

    public FilmServiceImpl(FilmStorage filmStorage, MpaStorage mpaStorage, GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    @Override
    public List<FilmDto> getAll() {
        List<FilmDto> films = filmStorage.getAll();
        List<Long> filmIds = films.stream()
                .map(FilmDto::id)
                .toList();
        List<FilmDto> filmsWhithGenres = films.stream()
                .map(film -> {
                    List<GenreDto> genres = genreStorage.findGenresByFilmId(film.id());
                    return new FilmDto(
                            film.id(),
                            film.name(),
                            film.description(),
                            film.releaseDate(),
                            film.duration(),
                            film.mpa(),
                            genres
                    );
                })
                .toList();
        log.info("Получение всех фильмов");
        return filmsWhithGenres;
    }

    @Override
    public FilmDto create(FilmDto filmDto) {
        if (filmDto.releaseDate().isBefore(FILM_DATE)) {
            throw new ValidationException("Дата выхода не может быть раньше " +
                    "28.12.1895 - даты выхода первого в истории фильма");
        }
        if (filmDto.mpa().getId() != null) {
            if (!mpaStorage.checkMpa(filmDto.mpa().getId())) {
                throw new NotFoundException("Рейтинг не найден");
            }
        }
        List<GenreDto> genres = Collections.emptyList();
        if (filmDto.genres() != null) {
            genres = filmDto.genres()
                    .stream()
                    .distinct()
                    .peek(g -> {
                        if (!genreStorage.checkGenre(g.id())) {
                            throw new NotFoundException("Жанр не найден");
                        }
                    })
                    .toList();
        }
        FilmDto createdFilm = filmStorage.create(filmDto);
        filmStorage.saveGenres(createdFilm.id(), genres);
        List<GenreDto> actualGenres = genreStorage.findGenresByFilmId(createdFilm.id());
        FilmDto filmWithGenres = new FilmDto(
                createdFilm.id(),
                createdFilm.name(),
                createdFilm.description(),
                createdFilm.releaseDate(),
                createdFilm.duration(),
                createdFilm.mpa(),
                actualGenres
        );
        log.info("Создание фильма {}", filmWithGenres);
        return filmWithGenres;
    }

    @Override
    public FilmDto update(FilmDto filmDto) {
        if (filmDto.mpa().getId() != null) {
            if (!mpaStorage.checkMpa(filmDto.mpa().getId())) {
                throw new NotFoundException("Рейтинг не найден");
            }
        }
        FilmDto updatedFilm = filmStorage.update(filmDto);
        if (filmDto.genres() != null) {
            List<GenreDto> genres = filmDto.genres();
            for (GenreDto g : genres) {
                if (!genreStorage.checkGenre(g.id())) {
                    throw new NotFoundException("Жанр не найден");
                }
            }
        }
        log.info("Обновление фильма {}", filmDto);
        return updatedFilm;
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
        List<FilmDto> films = filmStorage.getPopularFilms(count);
        List<FilmDto> filmsWhithGenres = films.stream()
                .map(film -> {
                    List<GenreDto> genres = genreStorage.findGenresByFilmId(film.id());
                    return new FilmDto(
                            film.id(),
                            film.name(),
                            film.description(),
                            film.releaseDate(),
                            film.duration(),
                            film.mpa(),
                            genres
                    );
                })
                .toList();
        log.info("Получение списка популярных фильмов начиная с 1 и до {}", count);
        return filmsWhithGenres;
    }

    @Override
    public FilmDto getFilmById(Long id) {
        FilmDto filmWithoutGenres = filmStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с " + id + " не найден"));
        List<GenreDto> genres = genreStorage.findGenresByFilmId(id);
        FilmDto resultFilm = new FilmDto(
                filmWithoutGenres.id(),
                filmWithoutGenres.name(),
                filmWithoutGenres.description(),
                filmWithoutGenres.releaseDate(),
                filmWithoutGenres.duration(),
                filmWithoutGenres.mpa(),
                genres
        );
        log.info("Получение фильма по id = {}", id);
        return resultFilm;
    }
}

