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
import ru.yandex.practicum.filmorate.dao.GenresDao;
import ru.yandex.practicum.filmorate.dao.LikesDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

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
    private final MpaDao mpaDao;
    private final GenresDao genresDao;

    @Override
    public Film getFilmsById(long id) {
        String sql = "select * from films where film_id = ?";
        List<Film> film = jdbcTemplate.query(sql, new FilmMapper(), id);

        if (!film.isEmpty()) {
            log.debug("Получен фильм с id - {}", id);
            return film.get(0);
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
            film.setMpa(mpaDao.getMpaById(rs.getLong("mpa_id")));
            film.getLikes().addAll(likesDao.getLikedUsersId(rs.getLong("film_id")));

            for (Long genreId : genresDao.getFilmGenresId(rs.getLong("film_id"))) {
                film.getGenres().add(genresDao.getGenreById(genreId));
            }

            return film;
        }
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