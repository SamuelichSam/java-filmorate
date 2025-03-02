package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.mpa.MpaDto;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    @GetMapping
    public List<MpaDto> findAll() {
        return mpaService.getAll();
    }

    @GetMapping("/{id}")
    public MpaDto getMpaById(@PathVariable Long id) {
        return mpaService.getMpaById(id);
    }
}