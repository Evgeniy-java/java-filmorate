package ru.yandex.practicum.filmorate.dao;

import java.util.Collection;

public interface FriendsDao {
    void addToFriends(long userId, long friendId);

    void deleteFromFriends(long userId, long friendId);

    Collection<Long> getFriendsIds(long userId);

    Collection<Long> getCommonFriendsIds(long userId, long friendId);
}
