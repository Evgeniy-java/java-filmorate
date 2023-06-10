package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.UserDao;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final FriendsDao friendsDao;


    //получить пользователя по id
    public User getUserById(long id) {
        return userDao.getUserById(id);
    }

    //получение списка всех пользователей
    public Collection<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    //создание пользователя
    public User createUser(User user) {
        return userDao.createUser(user);
    }

    //обновление пользователя
    public User updateUser(User user) {
        return userDao.updateUser(user);
    }

    //добавить пользователя в друзья
    public void addFriend(long userId, long friendId) {
        if (userDao.userExists(friendId)) {
            friendsDao.addToFriends(userId, friendId);
        } else {
            throw new NotFoundException(String.format("id %s друга не корректный", friendId));
        }
    }

    //удалить из друзей
    public void deleteFriend(long userId, long friendId) {
        if (userDao.userExists(friendId)) {
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