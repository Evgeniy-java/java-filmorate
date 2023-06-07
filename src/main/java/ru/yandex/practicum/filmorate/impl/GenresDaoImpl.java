package ru.yandex.practicum.filmorate.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenresDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenresDaoImpl implements GenresDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Genre> getAllGenres() {
        String sql = "select * from genres";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRow(rs));
    }

    @Override
    public Optional<Genre> getGenreById(long id) {
        // выполняем запрос к базе данных.
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from genres where genre_id = ?", id);

        // обрабатываем результат выполнения запроса
        if (genreRows.next()) {
            Genre genre = new Genre(
                    genreRows.getLong("genre_id"),
                    genreRows.getString("genre_name"));

            log.info("Найден жанр: {} {}", genreRows.getString("genre_id"), genreRows.getString("genre_name"));

            return Optional.of(genre);
        } else {
            log.debug("Жанр с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    private Genre mapRow(ResultSet rs) throws SQLException {
        return new Genre(rs.getLong("genre_id"), rs.getString("genre_name"));
    }
}