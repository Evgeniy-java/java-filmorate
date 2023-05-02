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

    //получение списка всех пользователей
    @GetMapping("/users")
    public Collection<User> getAllUsers() {
        log.info("Получен запрос Get /users, количество пользователей: {}", users.size());
        return users.values();
    }

    //создание пользователя
    @PostMapping(value = "/users")
    public User createUser(@Valid @RequestBody User user) {
        if (userValidate(user)) {
            users.put(user.setId(countId()), user);
            log.info("Пользователь добавлен: {}", user);
        }
        return user;
    }

    //обновление пользователя
    @PutMapping(value = "/users")
    public User updateUser(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            if (users.containsKey(user.getId())) {
                users.put(user.getId(), user);
                log.info("Пользователь добавлен: {}", user);
            }
        } else {
            log.warn("Пользователь: {} с таким id не существует", user.getName());
            throw new ValidationException("Пользователь: " + user.getName() + " с таким id не существует");
        }
        return user;
    }

    protected boolean userValidate(User user) {
        //логин не может быть пустым и содержать пробелы
        if (user.getLogin().isBlank() && user.getLogin().contains(" ")) {
            log.warn("пользователь с id: {} ,логин не может быть пустым и содержать пробелы", user.getId());
            throw new ValidationException("пользователь с id: " + user.getId() + ",логин не может быть пустым и содержать пробелы");
        }
        //Поле имя не заполнено, используем логин
        if (user.getName() == null || user.getName().isEmpty()) {
            log.info("Поле имя не заполнено, используем логин: {}", user.getLogin());
            user.setName(user.getLogin());
        }
        //указана неверная дата рождения пользователя
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("указана неверная дата рождения пользователя: {}", user.getName());
            throw new ValidationException("указана неверная дата рождения пользователя: " + user.getName());
        }
        return true;
    }
}