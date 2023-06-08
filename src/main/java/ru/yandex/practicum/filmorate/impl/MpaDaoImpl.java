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
        String sql = "select * from mpaa";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRow(rs));
    }

    @Override
    public Mpa getMpaById(long id) {
        // выполняем запрос к базе данных.
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from mpaa where mpaa_id = ?", id);

        // обрабатываем результат выполнения запроса
        if (genreRows.next()) {
            Mpa mpa = new Mpa(
                    genreRows.getLong("mpaa_id"),
                    genreRows.getString("name"));

            log.info("Найден жанр: {} {}", genreRows.getString("mpaa_id"), genreRows.getString("name"));

            return mpa;
        } else {
            log.debug("Жанр с идентификатором {} не найден.", id);
            return null;
        }
    }

    @Override
    public void mpaExisted(long id) {
        String sqlQuery = "select name from mpa WHERE mpaa_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (!rowSet.next()) {
            throw new NotFoundException(String.format("Не корректный id: %s mpa", id));
        }
    }

    private Mpa mapRow(ResultSet rs) throws SQLException {
        return new Mpa(rs.getLong("mpaa_id"), rs.getString("name"));
    }
}
