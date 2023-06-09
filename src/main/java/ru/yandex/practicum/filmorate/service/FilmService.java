package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.LikesDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    @Qualifier("FilmDbStorage")
    private final FilmStorage filmStorage;
    @Qualifier("UserDbStorage")
    private final UserStorage userStorage;
    private final LikesDao likesDao;

    //получить фильм по Id
    public Film getFilmsById(Long id) {
        if (filmStorage.filmExists(id)) {
            return filmStorage.getFilmsById(id);
        } else {
            throw new NotFoundException(String.format("id %s не корректный", id));
        }
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
    public void deleteFilmById(long id) {
        filmStorage.deleteFilmById(id);
    }

    //пользователь ставит лайк фильму
    public void addLikeFilm(Long id, long userId) {
        Film film = getFilmsById(id);

        if (userStorage.userExists(userId)) {
            film.getLikes().add(userId);
            log.info("пользователь с Id: {} поставил лайк фильму с id: {}", userId, id);
        } else {
            log.debug("Пользователь с id: {} не найден!", userId);
            throw new NotFoundException(String.format("Пользователь с id: %s не найден!", userId));
        }
    }

    //пользователь удаляет лайк
    public void deleteLikeFilm(long id, long userId) {
        Film film = getFilmsById(id);

        if (userStorage.userExists(userId)) {
            film.getLikes().remove(userId);
            log.debug("Пользователь с Id: {} удалил лайк фильму с Id: {}", userId, id);
        } else {
            log.debug("Пользователь с id: {} не найден!", userId);
            throw new NotFoundException(String.format("Пользователь с id: %s не найден!", userId));
        }
    }

    //возвращает список из первых count фильмов по количеству лайков
    public Collection<Film> getPopularFilms(long count) {
        List<Film> films = (List<Film>) filmStorage.getAllFilms();
        films.sort(Comparator.comparingInt(film -> likesDao.getLikedUsersId(film.getId()).size()));
        Collections.reverse(films);
        return films.subList(0, (int) Math.min(films.size(), count));
    }
}