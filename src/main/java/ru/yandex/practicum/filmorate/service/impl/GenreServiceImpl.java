package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.genre.GenreDto;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Slf4j
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreStorage genreStorage;

    public GenreServiceImpl(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    @Override
    public List<GenreDto> getAll() {
        log.info("Получение всех жанров");
        return genreStorage.getAll();
    }

    @Override
    public GenreDto getGenreById(Long id) {
        log.info("Получение жанра по id = {}", id);
        return genreStorage.getGenreById(id)
                .orElseThrow(() -> new NotFoundException("Жанра с таким id нет"));
    }
}
