package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    //получить фильм по Id
    public Film getFilmsById(Long id) {
        return filmStorage.getFilmsById(id);
    }

    //получение всех фильмов.
    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    //добавление фильма.
    public Film createFilm(@RequestBody Film film) {
        return filmStorage.createFilm(film);
    }

    //обновление фильма.
    public Film updateFilm(@RequestBody Film film) {
        return filmStorage.updateFilm(film);
    }
}