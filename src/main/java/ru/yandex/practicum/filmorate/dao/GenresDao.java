package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;


public interface GenresDao {
    Collection<Genre> getAllGenres();

    Genre getGenreById(long id);

    boolean mpaExists(long id);

    List<Long> getFilmGenresId(long filmId);
}