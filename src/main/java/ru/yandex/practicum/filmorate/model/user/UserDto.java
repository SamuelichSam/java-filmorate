package ru.yandex.practicum.filmorate.model.user;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserDto (
    Long id,
    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @ ")
    String email,
    @NotNull
    @NotBlank
    @Pattern(regexp = "^\\S+$", message = "Логин не может быть пустым и содержать пробелы")
    String login,
    String name,
    @NotNull
    @Past(message = "Дата рождения не может быть в будущем")
    LocalDate birthday
) {
}
