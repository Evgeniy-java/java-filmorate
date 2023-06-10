package ru.yandex.practicum.filmorate.daoTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.MpaDao;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/data-test.sql"})
public class MpaDaoTest {

    private final MpaDao mpaDao;

    @Test
    void getAllMpaTest() {
        assertEquals(5, mpaDao.getAllMpa().size());
    }

    @Test
    void getMpaByIdTest() {
        assertEquals(1, mpaDao.getMpaById(1).getId());
        assertEquals("G", mpaDao.getMpaById(1).getName());
    }
}