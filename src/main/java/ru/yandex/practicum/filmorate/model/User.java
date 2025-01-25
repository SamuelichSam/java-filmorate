package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Long id;
    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @ ")
    private String email;
    @NotNull
    @NotBlank
    @Pattern(regexp = "^\\S+$", message = "Логин не может быть пустым и содержать пробелы")
    private String login;
    private String name;
    @NotNull
    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();
    private Set<Long> likedFilms = new HashSet<>();
}


