package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
    private Long id;
    @NotNull(message = "Название фильма не может быть пустым")
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;
    @NotNull
    @Size(max = 200, message = "Описание фильма не должно превышать 200 символов")
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @NotNull
    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    private Integer duration;
    private Long like = 0L;

    public void addLike() {
        like++;
    }

    public void removeLike() {
        if (like > 0) {
            like--;
        }
    }
}
