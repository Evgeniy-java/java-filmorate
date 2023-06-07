package ru.yandex.practicum.filmorate.dao;

import java.util.Collection;

public interface LikesDao {
    void addLike(long userId, long filmId);

    void deleteLike(long userId, long filmId);
}
