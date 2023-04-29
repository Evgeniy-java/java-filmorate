package ru.yandex.practicum.filmorate.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    private final LocalDate dateTime = LocalDate.of(1895, 12, 28);

    protected final Map<Integer, Film> films = new HashMap<>();

    private int generationId = 0;

    private int countId() {
        return ++generationId;
    }

    //получение всех фильмов.
    @GetMapping("/films")
    public Collection<Film> getAllFilms() {
        log.info("Количество фильмов: {}", films.size());
        return films.values();
    }

    //добавление фильма.
    @PostMapping(value = "/films")
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        if (filmValidate(film)) {
            films.put(film.setId(countId()), film);
            log.info("Добавлен фильм: {}", film);
        }
        return film;
    }

    //обновление фильма.
    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        if (films.get(film.getId()).getId() == film.getId()) {
            if (filmValidate(film)) {
                films.put(film.setId(film.getId()), film);
                log.info("Изменен фильм>: {}", film);
            }
        }
        return film;
    }

    protected boolean filmValidate(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isEmpty()) {
            log.warn("Название не может быть пустым.");
            throw new ValidationException("Название не может быть пустым.");
        }
        if (film.getDescription().length() >= 200) {
            log.warn("Максимальная длина описания — 200 символов.");
            throw new ValidationException("Максимальная длина описания — 200 символов.");
        }
        if (film.getReleaseDate().isBefore(dateTime)) {
            log.warn("дата релиза — не раньше 28 декабря 1895 года.");
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() < 0) {
            log.warn("Продолжительность фильма должна быть положительной. Указана: {}", film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
        return true;
    }
}