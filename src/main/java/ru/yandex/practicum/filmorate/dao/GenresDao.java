package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.FilmGenres;

import java.util.Collection;
import java.util.stream.Collectors;

public interface GenresDao {
    Collection<FilmGenres> getAll();

}
