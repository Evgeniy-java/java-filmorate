package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Genre {
    private long id;
    private String name; //название жанра

    public Genre(long id, String name) {
        this.id = id;
        this.name = name;
    }
}