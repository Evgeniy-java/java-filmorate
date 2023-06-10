package ru.yandex.practicum.filmorate.daoTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/data-test.sql"})
public class UserDaoTest {

    private final UserDao userDao;

    @Test
    void getUserById() {
        assertThat(userDao.getUserById(1)).hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("email", "test1@mail.ru")
                .hasFieldOrPropertyWithValue("login", "login1");
    }

    @Test
    void getAllUsers() {
        assertThat(userDao.getAllUsers()).hasSize(2);
    }

    @Test
    void createUser() {
        User user3 = new User();
        user3.setEmail("test3@mail.ru");
        user3.setName("name3");
        user3.setBirthday(LocalDate.of(2003, 3, 3));

        userDao.createUser(user3);

        assertThat(userDao.getAllUsers()).hasSize(3);
        assertThat(userDao.getUserById(3))
                .hasFieldOrPropertyWithValue("id", 3L)
                .hasFieldOrPropertyWithValue("email", "film3")
                .hasFieldOrPropertyWithValue("login", "login1")
                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(2003, 3, 3));

    }

    @Test
    void updateUser() {
        User user2 = userDao.getUserById(2);
        user2.setName("untitled");

        userDao.updateUser(user2);
        assertThat(userDao.getUserById(2))
                .hasFieldOrPropertyWithValue("id", 2L)
                .hasFieldOrPropertyWithValue("email", "test2@mail.ru")
                .hasFieldOrPropertyWithValue("login", "login2")
                .hasFieldOrPropertyWithValue("name", "untitled");
    }
}