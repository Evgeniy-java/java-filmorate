package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
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
}