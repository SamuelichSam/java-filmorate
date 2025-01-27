package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Film create(Film entity) {
        log.info("Добавление фильма");
        entity.setId(getNextId());
        films.put(entity.getId(), entity);
        log.info("Добавлен фильм {}", entity);
        return entity;
    }

    @Override
    public Film update(Film entity) {
        log.info("Обновление фильма {}", entity);
        Film exFilm = films.get(entity.getId());
        if (films.containsKey(entity.getId())) {
            if (exFilm.getName() != null) {
                exFilm.setName(entity.getName());
            }
            if (exFilm.getDescription() != null) {
                exFilm.setDescription(entity.getDescription());
            }
            if (exFilm.getReleaseDate() != null) {
                exFilm.setReleaseDate(entity.getReleaseDate());
            }
            if (exFilm.getDuration() != null) {
                exFilm.setDuration(entity.getDuration());
            }
            films.put(entity.getId(), exFilm);
            log.info("Обновлен фильм {} с id - {}", entity, entity.getId());
            return exFilm;
        }
        log.error("Ошибка при обновлении фильма");
        throw new NotFoundException("Фильм с id - " + entity.getId() + " не найден");
    }

    @Override
    public Optional<Film> findById(Long id) {
        Film film = films.get(id);
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public void updateLikes(Film etity) {
        if (films.containsKey(etity.getId())) {
            Film exFilm = films.get(etity.getId());
            exFilm.setLike(etity.getLike());
        } else {
            throw new NotFoundException("Фильм не найден");
        }
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
