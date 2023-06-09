package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.FilmValidate;

import javax.validation.constraints.Positive;
import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    //получить фильм по Id
    @GetMapping("/{id}")
    public Film getFilmsById(
            @Positive(message = "некорректное число id")
            @PathVariable("id") long id) {
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
    public Film createFilm(@RequestBody Film film) {
        log.debug("Получен запрос Post /films ,добавление фильма: {}", film);

        if (FilmValidate.filmValidate(film)) {
            return filmService.createFilm(film);
        }
        return film;
    }

    //обновление фильма.
    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (FilmValidate.filmValidate(film)) {
            log.debug("Получен запрос Put /films ,обновление фильма: {}", film);
            return filmService.updateFilm(film);
        }
        return film;
    }

    //удаление фильма по id
    @DeleteMapping("/{id}")
    public void deleteFilmById(@PathVariable("id") long id) {
        log.debug("Получен запрос Delete /films/{id} ,удаление фильма по id: {}", id);
        filmService.deleteFilmById(id);
    }

    //пользователь ставит лайк фильму
    @PutMapping("/{id}/like/{userId}")
    public void addLikeFilm(
            @Positive(message = "id должен быть положительным числом") @PathVariable("id") long id,
            @Positive(message = "userId должен быть положительным числом") @PathVariable("userId") long userId) {
        log.debug("Получен запрос Put /films/{id}/like/{userId} " +
                        "пользователь с id: {} ставит лайк фильму с id: {}",
                userId, id);
        filmService.addLikeFilm(id, userId);
    }

    //пользователь удаляет лайк
    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFilm(
            @Positive(message = "id должен быть положительным числом") @PathVariable("id") long id,
            @Positive(message = "userId должен быть положительным числом") @PathVariable("userId") long userId) {
        log.debug("Получен запрос Delete /films/{id}/like/{userId} " +
                        "пользователь с id: {} убирает лайк к фильму с id: {}",
                userId, id);
        filmService.deleteLikeFilm(id, userId);
    }

    //возвращает список из первых count фильмов по количеству лайков
    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(
            @RequestParam(defaultValue = "10")
            @Positive(message = "не корректное значение count") long count) {
        log.debug("Получен запрос Get /films/popular?count={count} " +
                "на возврат списка из первых: {} фильмов по количеству лайков", count);

        return filmService.getPopularFilms(count);
    }
}