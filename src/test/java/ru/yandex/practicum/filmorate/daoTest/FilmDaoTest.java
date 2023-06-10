package ru.yandex.practicum.filmorate.daoTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/data-test.sql"})
class FilmDaoTest {
    private final FilmDao filmDao;
    private final MpaDao mpaDao;

    @Test
    void FilmById() {
        assertThat(filmDao.getFilmsById(1)).hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "film1")
                .hasFieldOrPropertyWithValue("description", "description1");

    }

    @Test
    void getAllFilms() {
        assertThat(filmDao.getAllFilms()).hasSize(2);
    }

    @Test
    void createFilm() {
        Film film3 = new Film();
        film3.setName("film3");
        film3.setDescription("description3");
        film3.setReleaseDate(LocalDate.of(2007, 7, 7));
        film3.setDuration(90);
        film3.setMpa(mpaDao.getMpaById(1));

        filmDao.createFilm(film3);

        assertThat(filmDao.getAllFilms()).hasSize(3);

        assertThat(filmDao.getFilmsById(3))
                .hasFieldOrPropertyWithValue("id", 3L)
                .hasFieldOrPropertyWithValue("name", "film3")
                .hasFieldOrPropertyWithValue("description", "description3")
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2007, 7, 7))
                .hasFieldOrPropertyWithValue("duration", 90L);

        assertThat(filmDao.getFilmsById(3).getMpa())
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "G");
    }

    @Test
    void deleteFilm() {
        filmDao.deleteFilmById(1);
        assertThat(filmDao.getAllFilms()).hasSize(1);
    }

    @Test
    void updateFilm() {
        Film film1 = filmDao.getFilmsById(1);
        film1.setName("filmUpdate");
        film1.setDescription("descriptionUpdate");

        filmDao.updateFilm(film1);

        assertEquals(1, filmDao.getFilmsById(1).getId());
        assertEquals("filmUpdate", filmDao.getFilmsById(1).getName());
        assertEquals("descriptionUpdate", filmDao.getFilmsById(1).getDescription());
    }
}