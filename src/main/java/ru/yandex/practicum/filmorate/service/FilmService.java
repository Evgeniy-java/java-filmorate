package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.LikesDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.UserDao;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmDao filmDao;
    private final UserDao userDao;
    private final LikesDao likesDao;

    //получить фильм по Id
    public Film getFilmsById(Long id) {
        if (filmDao.filmExists(id)) {
            return filmDao.getFilmsById(id);
        } else {
            throw new NotFoundException(String.format("id %s не корректный", id));
        }
    }

    //получение всех фильмов.
    public Collection<Film> getAllFilms() {
        return filmDao.getAllFilms();
    }

    //добавление фильма.
    public Film createFilm(Film film) {
        return filmDao.createFilm(film);
    }

    //обновление фильма.
    public Film updateFilm(Film film) {
        return filmDao.updateFilm(film);
    }

    //удаление фильма по id
    public void deleteFilmById(long id) {
        filmDao.deleteFilmById(id);
    }

    //пользователь ставит лайк фильму
    public void addLikeFilm(Long id, long userId) {
        Film film = filmDao.getFilmsById(id);

        if (userDao.userExists(userId)) {
            film.getLikes().add(userId);
            likesDao.addLike(id,userId);
            log.info("пользователь с Id: {} поставил лайк фильму с id: {}", userId, id);
        } else {
            log.debug("Пользователь с id: {} не найден!", userId);
            throw new NotFoundException(String.format("Пользователь с id: %s не найден!", userId));
        }
    }

    //пользователь удаляет лайк
    public void deleteLikeFilm(long id, long userId) {
        Film film = filmDao.getFilmsById(id);

        if (userDao.userExists(userId)) {
            film.getLikes().remove(userId);
            likesDao.deleteLike(id,userId);
            log.debug("Пользователь с Id: {} удалил лайк фильму с Id: {}", userId, id);
        } else {
            log.debug("Пользователь с id: {} не найден!", userId);
            throw new NotFoundException(String.format("Пользователь с id: %s не найден!", userId));
        }
    }

    //возвращает список из первых count фильмов по количеству лайков
    public Collection<Film> getPopularFilms(long count) {
        return likesDao.getPopularFilmsIds(count).stream()
                .map(filmDao::getFilmsById)
                .collect(Collectors.toList());
    }
}