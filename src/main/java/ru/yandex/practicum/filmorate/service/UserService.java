package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final FriendsDao friendsDao;


    //получить пользователя по id
    public User getUserById(long id) {
        return userStorage.getUserById(id);
    }

    //получение списка всех пользователей
    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    //создание пользователя
    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    //обновление пользователя
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    //добавить пользователя в друзья
    public void addFriend(long userId, long friendId) {
        if (userStorage.userExists(friendId)) {
            friendsDao.addToFriends(userId, friendId);
        } else {
            throw new NotFoundException(String.format("id %s друга не корректный", friendId));
        }
    }

    //удалить из дрзуей
    public void deleteFriend(long userId, long friendId) {
        if (userStorage.userExists(friendId)) {
            friendsDao.deleteFriend(userId, friendId);
        } else {
            throw new NotFoundException(String.format("id %s друга не корректный", friendId));
        }
    }

    //получить список друзей пользователя
    public Collection<User> getUserFriendsById(long id) {
        return friendsDao.getUserFriendsById(id);
    }

    //получить список друзей общих с другим пользователем
    public Collection<User> getCommonFriends(long firstUserId, long secondUserId) {
        return friendsDao.getCommonFriends(firstUserId, secondUserId);
    }
}