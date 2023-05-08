package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    //получение всех фильмов.
    Collection<Film> getAllFilms();

    //добавление фильма.
    Film createFilm(Film film);

    //обновление фильма.
    Film updateFilm(Film film);
}
