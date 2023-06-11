package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.UserDao;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
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
        log.debug("создание пользователь {}", user);
        return userDao.createUser(user);
    }

    //обновление пользователя
    public User updateUser(User user) {
        log.debug("обновлен пользователь {}", user);
        return userDao.updateUser(user);
    }

    //добавить пользователя в друзья
    public void addFriend(long userId, long friendId) {
        if (userDao.userExists(friendId)) {
            friendsDao.addToFriends(userId, friendId);
            log.debug("добавлен друг {} для пользователя {}", friendId, userId);
        } else {
            throw new NotFoundException(String.format("id %s друга не корректный", friendId));
        }
    }

    //удалить из друзей
    public void deleteFriend(long userId, long friendId) {
        if (userDao.userExists(friendId)) {
            friendsDao.deleteFriend(userId, friendId);
            log.debug("удален друг {} у пользователя {}", friendId, userId);
        } else {
            throw new NotFoundException(String.format("id %s друга не корректный", friendId));
        }
    }

    //получить список друзей пользователя
    public Collection<User> getUserFriendsById(long id) {
        return friendsDao.getUserFriendsById(id).stream()
                .map(userDao::getUserById)
                .collect(Collectors.toList());
    }

    //получить список друзей общих с другим пользователем
    public List<User> getCommonFriends(long firstUserId, long secondUserId) {
        return friendsDao.getCommonFriends(firstUserId, secondUserId);
    }
}