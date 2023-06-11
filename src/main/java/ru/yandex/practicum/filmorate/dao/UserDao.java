package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserDao {

    //получить пользователя по id
    User getUserById(long id);

    //получение списка всех пользователей
    Collection<User> getAllUsers();

    //создание пользователя
    User createUser(User user);

    //обновление пользователя
    User updateUser(User user);

    //проверка существует пользователь или нет по id
    boolean userExists(long id);
}