package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.film.Film;
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

    @Test
    public void findAllFilmsTest() {
        Film film1 = new Film();
        film1.setName("Film1");
        film1.setDescription("Desc1");
        film1.setDuration(2);
        film1.setReleaseDate(LocalDate.of(2000, 1, 1));
        film1.setMpa(new Mpa());
        film1.setGenres(List.of());
        Film film2 = new Film();
        film2.setName("Film2");
        film2.setDescription("Desc2");
        film2.setDuration(2);
        film2.setReleaseDate(LocalDate.of(2000, 1, 1));
        film2.setMpa(new Mpa());
        film2.setGenres(List.of());

        filmStorage.create(film1);
        filmStorage.create(film2);

        List<Film> films = filmStorage.getAll();
        System.out.println(films);
        assertThat(films).hasSize(2);
    }

    @Test
    public void createFilmTest() {
        Film film1 = new Film();
        film1.setName("Film1");
        film1.setDescription("Desc1");
        film1.setDuration(2);
        film1.setReleaseDate(LocalDate.of(2000, 1, 1));
        film1.setMpa(new Mpa());
        film1.setGenres(List.of());

        Film createdFilm = filmStorage.create(film1);

        assertThat(createdFilm.getName()).isEqualTo("Film1");
        assertThat(createdFilm.getDescription()).isEqualTo("Desc1");
        assertThat(createdFilm.getDuration()).isEqualTo(2);
        assertThat(createdFilm.getReleaseDate()).isEqualTo(LocalDate.of(2000, 1, 1));
    }

    @Test
    public void updateFilmTest() {
        Film film1 = new Film();
        film1.setName("Film1");
        film1.setDescription("Desc1");
        film1.setDuration(2);
        film1.setReleaseDate(LocalDate.of(2000, 1, 1));
        film1.setMpa(new Mpa());
        film1.setGenres(List.of());

        Film createdFilm = filmStorage.create(film1);
        createdFilm.setName("UPDfilm");
        createdFilm.setDescription("UPDdesc");
        createdFilm.setDuration(3);
        Film updatedFilm = filmStorage.update(createdFilm);

        assertThat(updatedFilm.getName()).isEqualTo("UPDfilm");
        assertThat(updatedFilm.getDescription()).isEqualTo("UPDdesc");
        assertThat(updatedFilm.getDuration()).isEqualTo(3);
    }

    @Test
    public void getFilmById() {
        Film film1 = new Film();
        film1.setName("Film1");
        film1.setDescription("Desc1");
        film1.setDuration(2);
        film1.setReleaseDate(LocalDate.of(2000, 1, 1));
        film1.setMpa(new Mpa());
        film1.setGenres(List.of());

        Film createdFilm = filmStorage.create(film1);
        Optional<Film> filmOptional = filmStorage.findById(createdFilm.getId());

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("name", "Film1"));
    }
}
