package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDbTests {

    @Test
    public void testFindUserById() {
        User user = new User(1, "email", "login", "name", LocalDate.now());

        assertEquals(1, user.getId());
    }
}