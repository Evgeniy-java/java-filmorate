package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {

    protected static LocalDate OLD_DATE_FILM = LocalDate.of(1895, 12, 28);

    protected final Map<Integer, Film> films = new HashMap<>();

    private int generationId = 0;

    private int countId() {
        return ++generationId;
    }

    //получение всех фильмов.
    @GetMapping("/films")
    public Collection<Film> getAllFilms() {
        log.debug("Получен запрос Get /films ,количество фильмов: {}", films.size());
        return films.values();
    }

    //добавление фильма.
    @PostMapping(value = "/films")
    public Film createFilm(@RequestBody Film film) {
        if (filmValidate(film)) {
            films.put(film.setId(countId()), film);
            log.info("Добавлен фильм: {}", film);
        }
        return film;
    }

    //обновление фильма.
    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            if (filmValidate(film)) {
                films.put(film.setId(film.getId()), film);
                log.info("Изменен фильм>: {}", film);
            }
        } else {
            log.warn("Фильма: {} с таким id не существует", film.getName());
            throw new ValidationException("Фильма: " + film.getName() + " с таким id не существует");
        }
        return film;
    }

    protected boolean filmValidate(Film film) {
        //название фильма не может быть пустым
        if (film.getName() == null || film.getName().isEmpty()) {
            log.warn("Фильм с id: {} ,название не может быть пустым.", film.getId());
            throw new ValidationException("Фильм с id: " + film.getId() + " ,название не может быть пустым.");
        }
        //длина описания фильма не более 200 символов
        if (film.getDescription().length() >= 200) {
            if (film.getName() != null) {
                log.warn("Перевышена длина описания фильма {} — максимальная длина 200 символов! Введено: {}",
                        film.getName(), film.getDescription().length());
                throw new ValidationException("Перевышена длина описания фильма " + film.getName() +
                        " - максимальная длина 200 символов! Введено: " + film.getDescription().length());
            } else
                log.warn("Перевышена длина описания фильма с id: {} — максимальная длина 200 символов! Введено: {}",
                        film.getId(), film.getDescription().length());
            throw new ValidationException("Перевышена длина описания фильма с id: " + film.getId() +
                    " - максимальная длина 200 символов! Введено: " + film.getDescription().length());
        }
        //дата релиза фильма не раньше 28.12.1895
        if (film.getReleaseDate().isBefore(OLD_DATE_FILM)) {
            log.warn("дата релиза фильма: {} должна быть — не раньше 28 декабря 1895 года.", film.getName());
            throw new ValidationException("дата релиза фильма: " + film.getName() +
                    " должна быть — не раньше 28 декабря 1895 года. Указана: " + film.getReleaseDate());
        }
        //продолжительность фильма должна быть положительной
        if (film.getDuration() < 0) {
            log.warn("Продолжительность фильма: {} должна быть положительной. Указана: {}", film.getName(), film.getDuration());
            throw new ValidationException("Продолжительность фильма: " + film.getName() +
                    " должна быть положительной. Указана: " + film.getDuration());
        }
        return true;
    }
}