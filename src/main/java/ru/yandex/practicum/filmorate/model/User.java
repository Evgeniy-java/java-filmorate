package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode
public class User {
    private long id; //целочисленный идентификатор
    @Email(message = "электронная почта не может быть пустой и должна содержать символ @")
    private String email; //электронная почта
    private String login; //логин пользователя
    private String name; //имя для отображения
    private LocalDate birthday; //дата рождения

    //список с информацией о том, что два пользователя являются друзьями
    private Set<Long> friends = new HashSet<>();

    public User(long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public long setId(long id) {
        this.id = id;
        return id;
    }
}