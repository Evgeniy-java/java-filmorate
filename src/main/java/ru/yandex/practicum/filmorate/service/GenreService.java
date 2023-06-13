package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenresDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreService {
    private final GenresDao genresDao;

    public Collection<Genre> getAllGenres() {
        return genresDao.getAllGenres();
    }

    public Genre getGenreById(long id) {
        if (genresDao.genreExists(id)) {
            log.debug("Получен id: {} genre", id);
            return genresDao.getGenreById(id);
        } else {
            log.debug("Не корректный id: {} genre", id);
            throw new NotFoundException(String.format("Не корректный id: %s genre", id));
        }
    }
}
