package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTest {

    private FilmController filmController;

    @BeforeEach
    void beforeEach() {
        FilmStorage filmStorage = new InMemoryFilmStorage();
        filmController = new FilmController(new FilmService(filmStorage, new InMemoryUserStorage()));
    }

    void createTestFilms() {
        Film film1 = new Film(1, "name", "description", LocalDate.of(2000, 11, 20), 100);
        filmController.createFilm(film1);
        System.out.println(filmController.getFilmsById(1));
    }

    //пустой список фильмов
    @Test
    void getAllFilms() {
        assertEquals(0, filmController.getAllFilms().size());
    }


    //добавление фильма
    @Test
    void createFilm() {
        createTestFilms();
        assertEquals(1, filmController.getAllFilms().size());
    }

    //обновление фильма
    @Test
    void updateFilm() {
        createTestFilms();

        Film filmUpdate = new Film(1, "nameUpdate", "descriptionUpdate", LocalDate.of(2001, 12, 21), 101);
        filmController.updateFilm(filmUpdate);
        assertEquals(1, filmController.getAllFilms().size());
        assertEquals(filmUpdate, filmController.getFilmsById(1));
    }
}