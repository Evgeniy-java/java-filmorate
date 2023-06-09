package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    protected final Map<Long, Film> films = new HashMap<>();

    private long generationId = 0;

    private Long countId() {
        return ++generationId;
    }

    //получить фильм по Id
    @Override
    public Film getFilmsById(long id) {
        if (!films.containsKey(id)) {
            log.warn("Не найден фильм с id: {}", id);
            throw new NotFoundException(String.format("Не найден фильм с id: %s", id));
        }
        return films.get(id);
    }

    //получение всех фильмов.
    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    //добавление фильма.
    @Override
    public Film createFilm(Film film) {
        films.put(film.setId(countId()), film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    //обновление фильма.
    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.setId(film.getId()), film);
            log.info("Изменен фильм>: {}", film);
        } else {
            log.warn("Фильма: {} с таким id не существует", film.getName());
            throw new ValidationException(String.format("Фильма: %s с таким id не существует", film.getName()));
        }
        return film;
    }

    //удаление фильма по id
    @Override
    public void deleteFilmById(long id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException(String.format("Фильм с Id: %s не найден", id));
        } else films.remove(id);
    }

    @Override
    public boolean filmExists(long id) {
        return films.containsKey(id);
    }
}