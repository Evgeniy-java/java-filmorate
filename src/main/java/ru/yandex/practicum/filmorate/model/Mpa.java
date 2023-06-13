package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Mpa {
    @NotNull
    private long id;
    private String name; //рейтинг Ассоциации кинокомпаний


    public Mpa(long id, String name) {
        this.id = id;
        this.name = name;
    }
}