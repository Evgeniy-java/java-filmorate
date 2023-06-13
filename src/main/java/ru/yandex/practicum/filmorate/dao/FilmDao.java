package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmDao {
    //получить фильм по Id
    Film getFilmsById(long id);

    //получение всех фильмов.
    Collection<Film> getAllFilms();

    //добавление фильма.
    Film createFilm(Film film);

    //обновление фильма.
    Film updateFilm(Film film);

    //удаление фильма по id
    void deleteFilmById(long id);

    //проверка существования фильма по id
    boolean filmExists(long id);
}