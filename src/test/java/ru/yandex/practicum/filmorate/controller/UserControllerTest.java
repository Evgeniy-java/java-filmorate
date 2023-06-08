package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest {

    private UserController userController;

    /*@BeforeEach
    void beforeEach() {
        UserStorage filmStorage = new InMemoryUserStorage();
        userController = new UserController(new UserService(filmStorage));
    }

    void createTestUsers() {
        User user1 = new User(1, "email@test.com", "login", "name", LocalDate.of(1999, 9, 9));
        userController.createUser(user1);
    }

    //Пустой список пользователей
    @Test
    void getAllUsers() {
        assertEquals(0, userController.getAllUsers().size());
    }

    //Добавление пользователя
    @Test
    void createUser() {
        User user1 = new User(1, "email@test.com", "login", "name", LocalDate.of(1999, 9, 9));
        userController.createUser(user1);
        assertEquals(1, userController.getAllUsers().size());
        assertEquals(user1, userController.getUserById(1));
    }

    //Обновление полльзователя
    @Test
    void updateUser() {
        createTestUsers();
        User userUpdate = new User(1, "updateemail@test.com", "update login", "update name", LocalDate.of(2000, 10, 10));
        userController.updateUser(userUpdate);
        assertEquals(1, userController.getAllUsers().size());
        assertEquals(userUpdate, userController.getUserById(1));
    }*/
}