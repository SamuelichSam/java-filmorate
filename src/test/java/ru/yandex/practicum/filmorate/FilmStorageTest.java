package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.film.FilmDto;
import ru.yandex.practicum.filmorate.model.mpa.Mpa;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorageImpl;
import ru.yandex.practicum.filmorate.storage.impl.GenreDbStorageImpl;
import ru.yandex.practicum.filmorate.storage.impl.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.impl.mapper.GenreRowMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorageImpl.class, FilmRowMapper.class, GenreDbStorageImpl.class, GenreRowMapper.class})
public class FilmStorageTest {
    private final FilmDbStorageImpl filmStorage;
    private static final LocalDate FILM_DATE = LocalDate.of(2000, 1, 1);

    @Test
    public void findAllFilmsTest() {
        FilmDto film1 = new FilmDto(null, "Film1", "Desc1", FILM_DATE, 2, new Mpa(), List.of());
        FilmDto film2 = new FilmDto(null, "Film2", "Desc2", FILM_DATE, 2, new Mpa(), List.of());

        filmStorage.create(film1);
        filmStorage.create(film2);

        List<FilmDto> films = filmStorage.getAll();
        System.out.println(films);
        assertThat(films).hasSize(2);
    }

    @Test
    public void createFilmTest() {
        FilmDto film1 = new FilmDto(null, "Film1", "Desc1", FILM_DATE, 2, new Mpa(), List.of());

        FilmDto createdFilm = filmStorage.create(film1);

        assertThat(createdFilm.name()).isEqualTo("Film1");
        assertThat(createdFilm.description()).isEqualTo("Desc1");
        assertThat(createdFilm.duration()).isEqualTo(2);
        assertThat(createdFilm.releaseDate()).isEqualTo(LocalDate.of(2000, 1, 1));
    }

    @Test
    public void updateFilmTest() {
        FilmDto film1 = new FilmDto(null, "Film1", "Desc1", FILM_DATE, 2, new Mpa(), List.of());

        FilmDto createdFilm = filmStorage.create(film1);
        FilmDto updateFilm1 = new FilmDto(createdFilm.id(), "UPDfilm", "UPDdesc", FILM_DATE, 3, new Mpa(), List.of());
        FilmDto updatedFilm = filmStorage.update(updateFilm1);

        assertThat(updatedFilm.name()).isEqualTo("UPDfilm");
        assertThat(updatedFilm.description()).isEqualTo("UPDdesc");
        assertThat(updatedFilm.duration()).isEqualTo(3);
    }

    @Test
    public void getFilmById() {
        FilmDto film1 = new FilmDto(null, "Film1", "Desc1", FILM_DATE, 2, new Mpa(), List.of());

        FilmDto createdFilm = filmStorage.create(film1);
        Optional<FilmDto> filmOptional = filmStorage.findById(createdFilm.id());

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("name", "Film1"));
    }
}

