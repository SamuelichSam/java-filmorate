package ru.yandex.practicum.filmorate.model.film;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import ru.yandex.practicum.filmorate.model.genre.GenreDto;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {
    public static Film toFilm(FilmDto filmDto) {
        var film = new Film();
        film.setId(filmDto.getId());
        film.setName(filmDto.getName());
        film.setDescription(filmDto.getDescription());
        film.setReleaseDate(filmDto.getReleaseDate());
        film.setDuration(filmDto.getDuration());
        film.setMpa(filmDto.getMpa());
        if (film.getGenres() != null) {
            List<Genre> genres = filmDto.getGenres()
                    .stream()
                    .map(genreDto -> {
                        Genre genre = new Genre();
                        genre.setId(genreDto.getId());
                        return genre;
                    })
                    .collect(Collectors.toList());
            film.setGenres(genres);
        }
        return film;

    }

    public static FilmDto toDto(Film film) {
        FilmDto dto = new FilmDto();
        dto.setId(film.getId());
        dto.setName(film.getName());
        dto.setDescription(film.getDescription());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setDuration(film.getDuration());
        dto.setMpa(film.getMpa());
        if (film.getGenres() != null) {
            List<GenreDto> genresDto = film.getGenres()
                    .stream()
                    .map(genre -> {
                        GenreDto genreDto = new GenreDto();
                        genreDto.setId(genre.getId());
                        genreDto.setName(genre.getName());
                        return genreDto;
                    })
                    .collect(Collectors.toList());
            dto.setGenres(genresDto);
        }
        return dto;
    }
}
