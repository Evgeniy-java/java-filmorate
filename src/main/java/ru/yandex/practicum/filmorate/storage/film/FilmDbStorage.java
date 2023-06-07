package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenresDao;
import ru.yandex.practicum.filmorate.dao.MpaaDao;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Qualifier
    private final MpaaDao mpaaDao;
    @Qualifier
    private final GenresDao genresDao;

    @Override
    public Film getFilmsById(long id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films where film_id = ?", id);

        if (filmRows.next()) {
            Film film = new Film(
                    filmRows.getLong("film_id"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    Objects.requireNonNull(filmRows.getDate("release_date")).toLocalDate(),
                    filmRows.getLong("duration"),
                    mpaaDao.getMpaaById(filmRows.getLong("mpaa_id"))
            );

            log.debug("Получен фильм: {} с id - {}", film.getName(), film.getId());
            return film;
        } else {
            log.warn("Фильма с id {} нет", id);
            throw new IncorrectParameterException(String.format("Фильма с id %s нет", id));
        }
    }

    @Override
    public Collection<Film> getAllFilms() {
        String sql = "select * from films";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film createFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");

        Map<String, Object> values = new HashMap<>();
        values.put("film_id", film.getId());
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("mpaa_id", film.getMpaa());

        long userId = (long) simpleJdbcInsert.executeAndReturnKey(values);
        film.setId(userId);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "update films set film_id = ?, name = ?, description = ?, release_date = ?," +
                "duration = ?, mpaa_id = ? where film_id = ?";
        int resNumber = jdbcTemplate.update(sql, film.getName(), film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getId());

        if (resNumber != 0) {
            log.debug("Обновлен фильм: {} с id - {}", film.getName(), film.getId());
            return film;
        } else {
            log.warn("Фильма с id {} нет", film.getId());
            throw new IncorrectParameterException(String.format("Фильма с id %s нет", film.getId()));
        }
    }

    @Override
    public void deleteFilmById(long id) {

    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Film film = new Film(rs.getLong("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getLong("duration"),
                mpaaDao.getMpaaById(rs.getLong("mpaa_id")));

        return film;
    }
}
