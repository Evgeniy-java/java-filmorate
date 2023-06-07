package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.GenresDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@RestController
@RequestMapping("/genre")
@RequiredArgsConstructor
public class GenreController {
    private final GenresDao genresDao;

    @GetMapping
    public Collection<Genre> getAllGenres(){
        return genresDao.getAllGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable long id){
        return genresDao.getGenreById(id);
    }
}
