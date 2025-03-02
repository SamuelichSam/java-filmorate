package ru.yandex.practicum.filmorate.model.film;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import ru.yandex.practicum.filmorate.model.genre.GenreDto;
import ru.yandex.practicum.filmorate.model.mpa.Mpa;

import java.time.LocalDate;
import java.util.List;


public record FilmDto(
    Long id,
    @NotNull(message = "Название фильма не может быть пустым")
    @NotBlank(message = "Название фильма не может быть пустым")
    String name,
    @NotNull
    @Size(max = 200, message = "Описание фильма не должно превышать 200 символов")
    String description,
    @NotNull
    LocalDate releaseDate,
    @NotNull
    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    Integer duration,
    Mpa mpa,
    List<GenreDto> genres
) {
}
