package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;

public interface UserStorage {

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

    Collection<User> getUserFriends(Set<Long> friendsId);
}