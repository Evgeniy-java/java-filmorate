package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

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
        User userById = userStorage.getUserById(userId);
        User friendById = userStorage.getUserById(friendId);

        userById.getFriends().add(friendId);
        friendById.getFriends().add(userId);
    }

    //удалить из дрзуей
    public void deleteFriend(long userId, long friendId) {
        User userById = userStorage.getUserById(userId);
        User friendById = userStorage.getUserById(friendId);

        userById.getFriends().remove(friendId);
        friendById.getFriends().remove(userId);
    }

    //получить список друзей пользователя
    public List<User> getUserFriendsById(long id) {
        Set<Long> userFriends = userStorage.getUserById(id).getFriends();

        return userFriends.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    //получить список друзей общих с другим пользователем
    public Collection<User> getCommonFriends(long firstUserId, long secondUserId) {
        //первый пользователь
        User firstUser = userStorage.getUserById(firstUserId);
        //второй пользователь
        User secondUser = userStorage.getUserById(secondUserId);

        Set<Long> commonFriends = new HashSet<>(firstUser.getFriends());
        commonFriends.retainAll(secondUser.getFriends());
        return commonFriends.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }
}