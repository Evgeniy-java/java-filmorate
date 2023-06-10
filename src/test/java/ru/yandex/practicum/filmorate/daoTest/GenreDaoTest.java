package ru.yandex.practicum.filmorate.daoTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.GenresDao;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/data-test.sql"})
public class GenreDaoTest {
    private final GenresDao genresDao;

    @Test
    void getAllGenres() {
        assertEquals(6, genresDao.getAllGenres().size());
    }

    @Test
    void getGenreById() {
        assertEquals(1, genresDao.getGenreById(1).getId());
        assertEquals("Комедия", genresDao.getGenreById(1).getName());
    }
}