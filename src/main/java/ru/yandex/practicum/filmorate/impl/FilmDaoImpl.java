package ru.yandex.practicum.filmorate.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.LikesDao;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
@Primary
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;

    private final LikesDao likesDao;

    @Override
    public Film getFilmsById(long id) {
        String sql = "SELECT f.*, m.name AS mpa_name FROM films f, mpa m WHERE f.mpa_id = m.mpa_id AND f.film_id = ?";
        List<Film> films = jdbcTemplate.query(sql, new FilmMapper(), id);

        if (!films.isEmpty()) {
            log.debug("Получен фильм с id - {}", id);
            Film film = films.get(0);
            String sqlGenre = "SELECT fg.film_id, g.genre_id, g.genre_name FROM film_genre AS fg, genres AS g WHERE fg.genre_id = g.genre_id AND fg.film_id";
            List<Genre> filmGenre = jdbcTemplate.query(sqlGenre, this::makeGenre);
            for (Genre genre : filmGenre) {
                film.getGenres().add(genre);
            }
            return film;
        } else {
            log.warn("Фильма с id {} нет", id);
            throw new IncorrectParameterException(String.format("Фильма с id %s нет в БД", id));
        }
    }

    @Override
    public Collection<Film> getAllFilms() {
        String sql = "SELECT f.*, m.name AS mpa_name FROM films f, mpa m WHERE f.mpa_id = m.mpa_id";
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
        String sql = "select count(film_id) from films where film_id = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, film.getId());

        if (count == 1) {
            String update = "update films set name = ?, " +
                    "description = ?, release_date = ?, duration = ?, mpa_id = ? where film_id = ?";

            jdbcTemplate.update(update,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());

            updateGenreFilmTable(film);

            log.debug("Обновлен фильм: {} с id - {}", film.getName(), film.getId());
            return film;
        } else {
            log.warn("Фильма с id {} нет", film.getId());
            throw new NotFoundException(String.format("Фильма с id %s нет", film.getId()));
        }
    }

    @Override
    public void deleteFilmById(long id) {
        String sql = "delete from films where film_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean filmExists(long id) {
        String sql = "select count (film_id) from films where film_id = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, id);

        return count != 0;
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
            film.getLikes().addAll(likesDao.getLikedUsersId(rs.getLong("film_id")));
            film.setMpa(new Mpa(rs.getLong("mpa_id"),
                    rs.getString("mpa_name")));

            return film;
        }
    }

    public Genre makeGenre(final ResultSet rs, final long rowNum) throws SQLException {
        final Genre genre = new Genre();
        genre.setId(rs.getLong("genre_id"));
        genre.setName(rs.getString("genre_name"));
        return genre;
    }

    private void updateGenreFilmTable(Film film) {
        String sql = "delete from film_genre where film_id = ?";
        jdbcTemplate.update(sql, film.getId());

        List<Long> genres = film.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate("insert into film_genre (film_id,genre_id) values (?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        preparedStatement.setLong(1, film.getId());
                        preparedStatement.setLong(2, genres.get(i));

                    }

                    @Override
                    public int getBatchSize() {
                        return genres.size();
                    }
                });
    }
}