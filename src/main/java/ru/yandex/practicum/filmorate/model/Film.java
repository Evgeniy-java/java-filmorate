package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode
public class Film {
    private long id; //целочисленный идентификатор
    private String name; //название
    private String description; //описание
    private LocalDate releaseDate;//дата релиза
    private long duration; //продолжительность фильма
    private Set<Long> likes = new HashSet<>(); //список понравившихся фильмов

    public Film(long id, String name, String description, LocalDate releaseDate, long duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public long setId(long id) {
        this.id = id;
        return id;
    }
}