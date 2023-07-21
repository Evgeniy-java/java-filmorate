package ru.yandex.practicum.filmorate.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
@RequiredArgsConstructor
@Slf4j
public class MpaDaoImpl implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Mpa> getAllMpa() {
        String sql = "select * from mpa";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRow(rs));
    }

    @Override
    public Mpa getMpaById(long id) {
        // выполняем запрос к базе данных.
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("select * from mpa where mpa_id = ?", id);

        // обрабатываем результат выполнения запроса
        if (mpaRows.next()) {
            Mpa mpa = new Mpa(
                    mpaRows.getLong("mpa_id"),
                    mpaRows.getString("name"));

            log.info("Найдена категория фильма {} с Id {}", mpaRows.getString("name"), mpaRows.getString("mpa_id"));

            return mpa;
        } else {
            log.debug("Категория фильма с Id {} не найден.", id);
            throw new NotFoundException(String.format("Категория фильма с Id %s не найден.", id));
        }
    }

    @Override
    public boolean mpaExists(long id) {
        String sql = "select count (name) from mpa where mpa_id = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, id);
        return count != 0;
    }

    private Mpa mapRow(ResultSet rs) throws SQLException {
        return new Mpa(rs.getLong("mpa_id"), rs.getString("name"));
    }
}
