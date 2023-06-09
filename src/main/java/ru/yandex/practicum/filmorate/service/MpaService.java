package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class MpaService {
    private final MpaDao mpaDao;

    public Collection<Mpa> getAllMpa() {
        return mpaDao.getAllMpa();
    }

    public Mpa getMpaById(long id) {
        if (mpaDao.mpaExists(id)) {
            return mpaDao.getMpaById(id);
        } else {
            log.debug("Не корректный id: {} mpa", id);
            throw new NotFoundException(String.format("Не корректный id: %s mpa", id));
        }
    }
}
