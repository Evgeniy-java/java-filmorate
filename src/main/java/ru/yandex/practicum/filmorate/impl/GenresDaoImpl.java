package ru.yandex.practicum.filmorate.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenresDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

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
    public Genre getGenreById(long id) {
        // выполняем запрос к базе данных.
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from genres where genre_id = ?", id);

        // обрабатываем результат выполнения запроса
        if (genreRows.next()) {
            Genre genre = new Genre(
                    genreRows.getLong("genre_id"),
                    genreRows.getString("genre_name"));

            log.info("Найден жанр {} c id {}", genreRows.getString("genre_name"), genreRows.getString("genre_id"));

            return genre;
        } else {
            log.debug("Жанр с id {} не найден.", id);
            throw new NotFoundException(String.format("Жанр с id %s не найден.", id));
        }
    }

    @Override
    public boolean mpaExists(long id) {
        String sql = "select count (genre_name) from genres where genre_id = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, id);
        return count != 0;
    }

    private Genre mapRow(ResultSet rs) throws SQLException {
        return new Genre(rs.getLong("genre_id"), rs.getString("genre_name"));
    }

    public List<Long> getFilmGenresId(long filmId) {
        String sql = "select genre_id from film_genre where film_id = ?";
        return jdbcTemplate.queryForList(sql, Long.class, filmId);
    }
}