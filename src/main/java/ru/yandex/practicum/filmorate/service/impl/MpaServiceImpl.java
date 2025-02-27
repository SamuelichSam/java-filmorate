package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.mpa.MpaDto;
import ru.yandex.practicum.filmorate.model.mpa.MpaMapper;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Slf4j
@Service
public class MpaServiceImpl implements MpaService {
    private final MpaStorage mpaStorage;

    public MpaServiceImpl(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    @Override
    public List<MpaDto> getAll() {
        log.info("Получение всех рейтингов MPA");
        return mpaStorage.getAll()
                .stream()
                .map(MpaMapper::toDto)
                .toList();
    }

    @Override
    public MpaDto getMpaById(Long id) {
        log.info("Получение рейтинга по id = {}", id);
        return mpaStorage.getMpaById(id)
                .map(MpaMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Рейтинга с таким id нет"));
    }
}
