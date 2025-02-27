package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.mpa.MpaDto;

import java.util.List;

public interface MpaService {
    List<MpaDto> getAll();

    MpaDto getMpaById(Long id);
}
