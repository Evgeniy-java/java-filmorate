package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

public interface MpaDao {
    Collection<Mpa> getAllMpa();

    Mpa getMpaById(long id);

    //проверка существования фильма по id
    boolean mpaExists(long id);
}
