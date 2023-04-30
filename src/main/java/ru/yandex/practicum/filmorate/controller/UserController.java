package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class UserController {

    protected final Map<Integer, User> users = new HashMap<>();

    private int generationId = 0;

    private int countId() {
        return ++generationId;
    }

    //получение списка всех пользователей.
    @GetMapping("/users")
    public Collection<User> getAllUsers() {
        log.info("Получен запрос Get /users, количество пользователей: {}", users.size());
        return users.values();
    }

    //создание пользователя.
    @PostMapping(value = "/users")
    public User createUser(@Valid @RequestBody User user) throws ValidationException {
        if (userValidate(user)) {
            users.put(user.setId(countId()), user);
            log.info("Пользователь добавлен: {}", user);
        }
        return user;
    }

    //обновление пользователя
    @PutMapping(value = "/users")
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        if (users.get(user.getId()).getId() == user.getId()) {
            if (userValidate(user)) {
                users.put(user.getId(), user);
                log.info("Пользователь добавлен: {}", user);
            }
        }
        return user;
    }

    protected boolean userValidate(User user) throws ValidationException{
        if (user.getLogin().isBlank() && user.getLogin().contains(" ")) {
            log.warn("логин не может быть пустым и содержать пробелы");
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            log.info("Поле имя не заполнено, используем логин: {}", user.getLogin());
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("указана неверная дата рождения");
            throw new ValidationException("указана неверная дата рождения");
        }
        return true;
    }
}