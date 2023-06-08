package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenresDao;
import ru.yandex.practicum.filmorate.dao.LikesDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    private final LikesDao likesDao;
    private final MpaDao mpaDao;
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
                    mpaDao.getMpaById(filmRows.getLong("mpa_id"))
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
        return jdbcTemplate.query(sql, new FilmMapper());
    }

    @Override
    public Film createFilm(Film film) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");

        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("mpa_id", film.getMpa().getId());

        Long filmId = (Long) insert.executeAndReturnKey(values);

        film.setId(filmId);

        updateGenreFilmTable(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "update films set name = ?, description = ?, release_date = ?," +
                "duration = ?, mpa_id = ? where film_id = ?";
        int resNumber = jdbcTemplate.update(sql, film.getName(), film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getId());

        if (resNumber != 0) {
            log.debug("Обновлен фильм: {} с id - {}", film.getName(), film.getId());
            updateGenreFilmTable(film);
            return film;
        } else {
            log.warn("Фильма с id {} нет", film.getId());
            throw new IncorrectParameterException(String.format("Фильма с id %s нет", film.getId()));
        }
    }

    @Override
    public void deleteFilmById(long id) {
        String sql = "delete from film where film_id = ?";
        jdbcTemplate.update(sql, id);
    }

    private class FilmMapper implements RowMapper<Film> {
        @Override
        public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
            Film film = new Film();
            film.setId(rs.getLong("film_id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate((rs.getDate("release_date").toLocalDate()));
            film.setDuration(rs.getLong("duration"));
            film.setMpa(mpaDao.getMpaById(rs.getLong("mpa_id")));
            film.getLikes().addAll(likesDao.getLikedUsersId(rs.getLong("film_id")));

            String genresQuery = "select genre_id from film_genre where film_id = ?";
            List<Long> genresIds = jdbcTemplate.queryForList(genresQuery, Long.class, film.getId());
            for (Long genreId : genresIds) {
                film.getFilmGenres().add(genresDao.getGenreById(genreId));
            }
            return film;
        }
    }

    private void updateGenreFilmTable(Film film) {
        String sql = "delete from film_genre where film_id = ?";
        jdbcTemplate.update(sql, film.getId());

        String insert = "insert into film_genre (film_id,genre_id) values (?,?)";
        for (Genre genre : film.getFilmGenres()) {
            jdbcTemplate.update(insert, film.getId(), genre.getId());
        }
    }
}
