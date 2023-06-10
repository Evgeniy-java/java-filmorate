package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FriendsDao {
    void addToFriends(long userId, long friendId);

    void deleteFriend(long userId, long friendId);

    Collection<User> getUserFriendsById(long userId);

    Collection<User> getCommonFriends(long userId, long friendId);
}