package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface FriendsDao {
    void addToFriends(long userId, long friendId);

    void deleteFriend(long userId, long friendId);

    Collection<? extends Long> getUserFriendsById(long userId);

    List<User> getCommonFriends(long userId, long friendId);
}