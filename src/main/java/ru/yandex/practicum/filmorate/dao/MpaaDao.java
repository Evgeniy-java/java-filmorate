package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Mpaa;

import java.util.Collection;
import java.util.Optional;

public interface MpaaDao {
    Collection<Mpaa> getAllMpaa();

    Optional<Mpaa> getMpaaById(long id);
}
