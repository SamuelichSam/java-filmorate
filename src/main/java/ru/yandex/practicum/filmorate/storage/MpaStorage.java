package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.mpa.MpaDto;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {
    List<MpaDto> getAll();

    Optional<MpaDto> getMpaById(Long id);

    Boolean checkMpa(Long id);
}
