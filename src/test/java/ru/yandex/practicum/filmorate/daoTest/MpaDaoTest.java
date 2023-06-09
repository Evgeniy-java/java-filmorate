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
@Sql(scripts = {"/schema-test.sql","/data-test.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class MpaDaoTest {

    private final MpaDao mpaDao;

    @Test
    void getAllMpa() {
        assertEquals(5, mpaDao.getAllMpa().size());
    }

    @Test
    void getMpaById() {
        assertEquals(1, mpaDao.getMpaById(1).getId());
        assertEquals("G", mpaDao.getMpaById(1).getName());
    }
}