package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTests {

    InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();

    @Test
    public void testUpdateFilm() {
        Film film = new Film();
        film.setId(1L);
        film.setName("Test");
        film.setDescription("Test description");
        film.setReleaseDate(LocalDate.of(2025,1,13));
        film.setDuration(90);
        inMemoryFilmStorage.create(film);
        Film updFilm = new Film();
        updFilm.setId(film.getId());
        updFilm.setName("updTest");
        updFilm.setDescription("upd Test description");
        updFilm.setReleaseDate(LocalDate.of(2025,1,13));
        updFilm.setDuration(90);

        Film resFilm = inMemoryFilmStorage.update(updFilm);

        assertEquals(updFilm, resFilm);
    }
}
