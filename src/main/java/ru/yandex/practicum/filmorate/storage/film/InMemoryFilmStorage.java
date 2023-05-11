package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    protected static LocalDate OLD_DATE_FILM = LocalDate.of(1895, 12, 28);

    protected final Map<Long, Film> films = new HashMap<>();

    private long generationId = 0;

    private Long countId() {
        return ++generationId;
    }

    //получить фильм по Id
    @Override
    public Film getFilmsById(long id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Не найден фильм с id: " + id);
        }
        if (id <= 0) {
            throw new IncorrectParameterException("id");
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
        if (filmValidate(film)) {
            films.put(film.setId(countId()), film);
            log.info("Добавлен фильм: {}", film);
        }
        return film;
    }

    //обновление фильма.
    @Override
    public Film updateFilm(Film film) {
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

    //удаление фильма по id
    @Override
    public void deleteFilmById(long id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильм с Id: " + id + " не найден");
        } else films.remove(id);
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