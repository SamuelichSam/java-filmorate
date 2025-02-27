package ru.yandex.practicum.filmorate.model.mpa;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MpaMapper {
    public static Mpa toMpa(MpaDto mpaDto) {
        var mpa = new Mpa();
        mpa.setId(mpa.getId());
        mpa.setName(mpa.getName());
        return mpa;
    }

    public static MpaDto toDto(Mpa mpa) {
        MpaDto dto = new MpaDto();
        dto.setId(mpa.getId());
        dto.setName(mpa.getName());
        return dto;
    }
}
