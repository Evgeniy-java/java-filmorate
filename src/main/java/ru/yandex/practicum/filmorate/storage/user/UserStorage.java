package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    //получение списка всех пользователей
    Collection<User> getAllUsers();

    //создание пользователя
    User createUser(User user);

    //обновление пользователя
    User updateUser(User user);
}
