package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    protected final Map<Integer, User> users = new HashMap<>();

    private int generationId = 0;

    private int countId() {
        return ++generationId;
    }

    //получение списка всех пользователей.
    @GetMapping("/users")
    public Collection<User> getAllUsers() {
        log.info("Количество пользователей: {}", users.size());
        return users.values();
    }

    //создание пользователя.
    @PostMapping(value = "/users")
    public User createUser(@RequestBody User user) throws InvalidEmailException, ValidationException {
        if (userValidate(user)) {
            users.put(user.setId(countId()), user);
            log.info("Пользователь добавлен: {}", user);
        }
        return user;
    }

    //обновление пользователя
    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user) throws InvalidEmailException, ValidationException {
        if (users.get(user.getId()).getId() == user.getId()) {
            if (userValidate(user)) {
                users.put(user.getId(), user);
                log.info("Пользователь добавлен: {}", user);
            }
        }
        return user;
    }

    protected boolean userValidate(User user) throws ValidationException, InvalidEmailException {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.warn("электронная почта не может быть пустой.");
            throw new InvalidEmailException("электронная почта не может быть пустой.");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("электронная почта должна содержать символ @");
            throw new InvalidEmailException("электронная почта должна содержать символ @");
        }
        if (user.getLogin().isBlank() && user.getLogin().contains(" ")) {
            log.warn("логин не может быть пустым и содержать пробелы");
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            log.info("Поле имя не заполнено, используем логин");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("указана неверная дата рождения");
            throw new ValidationException("указана неверная дата рождения");
        }
        return true;
    }
}