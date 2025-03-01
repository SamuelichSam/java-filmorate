package ru.yandex.practicum.filmorate.model.genre;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenreMapper {
    public static Genre toGenre(GenreDto genreDto) {
        var genre = new Genre();
        genre.setId(genre.getId());
        genre.setName(genre.getName());
        return genre;
    }

    public static GenreDto toDto(Genre genre) {
        return new GenreDto(
        genre.getId(),
        genre.getName()
        );
    }
}
