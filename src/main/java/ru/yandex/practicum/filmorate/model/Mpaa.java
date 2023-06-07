package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Mpaa {
    private long id;
    private String name; //рейтинг Ассоциации кинокомпаний

    public Mpaa(long id, String name) {
        this.id = id;
        this.name = name;
    }
}