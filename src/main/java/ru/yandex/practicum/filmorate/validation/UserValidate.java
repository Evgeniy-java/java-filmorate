package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class UserValidate {
    public static boolean userValidate(User user) {
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
