package ru.yandex.practicum.filmorate.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaaDao;
import ru.yandex.practicum.filmorate.model.Mpaa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MpaaDaoImpl implements MpaaDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Mpaa> getAllMpaa() {
        String sql = "select * from mpaa";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRow(rs));
    }

    @Override
    public Optional<Mpaa> getMpaaById(long id) {
        // выполняем запрос к базе данных.
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from mpaa where mpaa_id = ?", id);

        // обрабатываем результат выполнения запроса
        if (genreRows.next()) {
            Mpaa mpaa = new Mpaa(
                    genreRows.getLong("mpaa_id"),
                    genreRows.getString("name"));

            log.info("Найден жанр: {} {}", genreRows.getString("mpaa_id"), genreRows.getString("name"));

            return Optional.of(mpaa);
        } else {
            log.debug("Жанр с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    private Mpaa mapRow(ResultSet rs) throws SQLException {
        return new Mpaa(rs.getLong("mpaa_id"), rs.getString("name"));
    }
}
