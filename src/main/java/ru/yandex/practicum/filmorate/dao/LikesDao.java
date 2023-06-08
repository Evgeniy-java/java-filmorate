package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface LikesDao {
    void addLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    Collection<Long> getLikedUsersId(long filmId);
}
