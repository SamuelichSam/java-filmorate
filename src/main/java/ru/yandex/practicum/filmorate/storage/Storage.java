package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.Optional;

public interface Storage<T> {
    Collection<T> getAll();

    T create(T entity);

    T update(T entity);

    Optional<T> findById(Long id);

    void updateLikes(T etity);
}
