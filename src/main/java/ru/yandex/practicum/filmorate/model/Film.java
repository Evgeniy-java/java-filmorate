package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode
public class Film {
    private int id; //целочисленный идентификатор
    private String name; //название
    private String description; //описание
    private LocalDate releaseDate;//дата релиза
    private long duration; //продолжительность фильма

    public Film(int id, String name, String description, LocalDate releaseDate, long duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public int setId(int id) {
        this.id = id;
        return id;
    }
}