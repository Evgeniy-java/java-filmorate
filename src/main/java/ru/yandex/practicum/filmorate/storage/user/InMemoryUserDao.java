package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.UserValidate;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserDao implements UserDao {
    //коллекция пользователей
    protected final Map<Long, User> users = new HashMap<>();

    private long generationId = 0;

    //последовательная генерация id
    private Long countId() {
        return ++generationId;
    }

    //получить пользователя по id
    @Override
    public User getUserById(long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Не найден пользователь с id: " + id);
        }
        return users.get(id);
    }

    //получение списка всех пользователей
    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    //создание пользователя
    @Override
    public User createUser(User user) {
        if (UserValidate.userValidate(user)) {
            users.put(user.setId(countId()), user);
        }
        return user;
    }

    //обновление пользователя
    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            if (users.containsKey(user.getId())) {
                users.put(user.getId(), user);
            }
        } else {
            throw new ValidationException("Пользователь: " + user.getName() + " с таким id не существует");
        }
        return user;
    }

    //проверка существует пользователь или нет по id
    @Override
    public boolean userExists(long id) {
        return users.containsKey(id);
    }
}