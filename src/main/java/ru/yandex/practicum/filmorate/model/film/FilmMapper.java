package ru.yandex.practicum.filmorate.model.film;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import ru.yandex.practicum.filmorate.model.genre.GenreDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {
    public static Film toFilm(FilmDto filmDto) {
        var film = new Film();
        film.setId(filmDto.id());
        film.setName(filmDto.name());
        film.setDescription(filmDto.description());
        film.setReleaseDate(filmDto.releaseDate());
        film.setDuration(filmDto.duration());
        film.setMpa(filmDto.mpa());
        if (film.getGenres() != null) {
            List<Genre> genres = filmDto.genres()
                    .stream()
                    .map(genreDto -> {
                        Genre genre = new Genre();
                        genre.setId(genreDto.id());
                        return genre;
                    })
                    .collect(Collectors.toList());
            film.setGenres(genres);
        }
        return film;

    }

    public static FilmDto toDto(Film film) {
        List<GenreDto> genresDto = Optional.ofNullable(film.getGenres())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(genre -> new GenreDto(genre.getId(), genre.getName()))
                .toList();
        return new FilmDto(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa(),
                genresDto
        );
    }
}


        /*dto.setId(film.getId());
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
*/