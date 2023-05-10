package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
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
    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    //обновление фильма.
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    //удаление фильма по id
    public void deleteFilmById(Long id) {
        filmStorage.deleteFilmById(id);
    }

    //пользователь ставит лайк фильму
    public void addLikeFilm(Long Id, Long userId) {
        Film film = filmStorage.getFilmsById(Id);

        if (userStorage.getUserById(userId) == null) {
            log.warn("Пользователь с id: {} не найден!", userId);
            throw new NotFoundException(String.format("Пользователь с id: %s не найден!", userId));
        }
        film.getLikes().add(userId);
    }

    //пользователь удаляет лайк
    public void deleteLikeFilm(Long Id, Long userId) {
        Film film = filmStorage.getFilmsById(Id);

        if (film.getLikes() == null) {
            log.warn("Лайк от пользователя с id: {} не найден!", userId);
            throw new NotFoundException(String.format("Лайк от пользователя с id: %s не найден!", userId));
        }
        film.getLikes().remove(userId);
    }

    //возвращает список из первых count фильмов по количеству лайков
    public Collection<Film> getPopularFilms(@Positive Long count) {
        return filmStorage.getAllFilms().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}