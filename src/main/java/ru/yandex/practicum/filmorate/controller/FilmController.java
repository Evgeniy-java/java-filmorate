package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController{
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    //получить фильм по Id
    @GetMapping("/{id}")
    public Film getFilmsById(@PathVariable("id") final Long id) {
        log.debug("Получен запрос Get /films/{id} ,получение фильма по id: {}", id);
        return filmService.getFilmsById(id);
    }

    //получение всех фильмов.
    @GetMapping
    public Collection<Film> getAllFilms() {
        log.debug("Получен запрос Get /films ,количество фильмов: {}", filmService.getAllFilms().size());
        return filmService.getAllFilms();
    }

    //добавление фильма.
    @PostMapping
    public Film createFilm(@RequestBody final Film film) {
        log.debug("Получен запрос Post /films ,добавление фильма: {}", film);
        return filmService.createFilm(film);
    }

    //обновление фильма.
    @PutMapping
    public Film updateFilm(@RequestBody final Film film) {
        log.debug("Получен запрос Put /films ,обновление фильма: {}", film);
        return filmService.updateFilm(film);
    }
}