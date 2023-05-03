package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import java.time.LocalDate;

@Data
@EqualsAndHashCode
public class User {
    private int id; //целочисленный идентификатор
    @Email(message = "электронная почта не может быть пустой и должна содержать символ @")
    private String email; //электронная почта
    private String login; //логин пользователя
    private String name; //имя для отображения
    private LocalDate birthday; //дата рождения

    public User(int id, String email, String login, String name, LocalDate birthday) {

        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public int setId(int id) {
        this.id = id;
        return id;
    }
}