package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ValidationException;
import java.time.LocalDate;

@Slf4j
public class FilmValidate {
    protected static LocalDate OLD_DATE_FILM = LocalDate.of(1895, 12, 28);

    public static boolean filmValidate(Film film) {
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
