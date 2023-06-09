package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
@RequiredArgsConstructor
public class Film {
    private long id; //целочисленный идентификатор
    private String name; //название
    private String description; //описание
    private LocalDate releaseDate;//дата релиза
    private long duration; //продолжительность фильма
    private Set<Long> likes = new HashSet<>(); //список понравившихся фильмов

    private Mpa mpa; //категория фильма
    private Set<Genre> genres = new TreeSet<>(Comparator.comparingLong(Genre::getId)); //список жанров

    public long setId(long id) {
        this.id = id;
        return id;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres.addAll(genres);
    }
}