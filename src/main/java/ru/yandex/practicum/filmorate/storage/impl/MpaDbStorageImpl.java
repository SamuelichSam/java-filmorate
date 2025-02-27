package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.mpa.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaDbStorageImpl extends BaseDbStorage<Mpa> implements MpaStorage {
    private static final String FIND_ALL_QUERY = "SELECT * " +
            "FROM mpa";
    private static final String FIND_BY_ID_QUERY = "SELECT * " +
            "FROM mpa " +
            "WHERE id = ?";
    private static final String CHECK_MPA_QUERY = "SELECT EXISTS " +
            "(SELECT id " +
            "FROM mpa " +
            "WHERE id = ?)";

    public MpaDbStorageImpl(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<Mpa> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Mpa> getMpaById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public Boolean checkMpa(Long id) {
        return jdbc.queryForObject(CHECK_MPA_QUERY, Boolean.class, id);
    }
}