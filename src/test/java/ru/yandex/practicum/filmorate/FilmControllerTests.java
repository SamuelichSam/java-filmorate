package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTests {

    FilmController filmController = new FilmController();

    @Test
    public void testEmptyName() {
        Film film = new Film();
        film.setId(1L);
        film.setDescription("Test description");
        film.setReleaseDate(LocalDate.of(2025,1,13));
        film.setDuration(90);

        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    public void testDescriptionLength() {
        String description = "a".repeat(201);
        Film film = new Film();
        film.setId(1L);
        film.setName("Test");
        film.setDescription(description);
        film.setReleaseDate(LocalDate.of(2025,1,13));
        film.setDuration(90);

        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    public void testReleaseDate() {
        Film film = new Film();
        film.setId(1L);
        film.setName("Test");
        film.setDescription("Test description");
        film.setReleaseDate(LocalDate.of(1895,12,27));
        film.setDuration(90);

        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    public void testDuration() {
        Film film = new Film();
        film.setId(1L);
        film.setName("Test");
        film.setDescription("Test description");
        film.setReleaseDate(LocalDate.of(2025,1,13));
        film.setDuration(-90);

        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    public void testUpdateFilm() {
        Film film = new Film();
        film.setId(1L);
        film.setName("Test");
        film.setDescription("Test description");
        film.setReleaseDate(LocalDate.of(2025,1,13));
        film.setDuration(90);
        filmController.create(film);
        Film updFilm = new Film();
        updFilm.setId(film.getId());
        updFilm.setName("updTest");
        updFilm.setDescription("upd Test description");
        updFilm.setReleaseDate(LocalDate.of(2025,1,13));
        updFilm.setDuration(90);

        Film resFilm = filmController.update(updFilm);

        assertEquals(updFilm, resFilm);
    }
}
