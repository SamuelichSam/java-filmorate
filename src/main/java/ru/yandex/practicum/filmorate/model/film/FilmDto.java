package ru.yandex.practicum.filmorate.model.film;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.genre.GenreDto;
import ru.yandex.practicum.filmorate.model.mpa.Mpa;

import java.time.LocalDate;
import java.util.List;

@Data
public class FilmDto {
    Long id;
    String name;
    String description;
    LocalDate releaseDate;
    Integer duration;
    Mpa mpa;
    List<GenreDto> genres;
}
