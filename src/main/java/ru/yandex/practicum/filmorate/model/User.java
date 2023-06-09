package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private long id; //целочисленный идентификатор
    @Email(message = "электронная почта не может быть пустой и должна содержать символ @")
    private String email; //электронная почта
    private String login; //логин пользователя
    private String name; //имя для отображения
    private LocalDate birthday; //дата рождения
    private FriendsStatus friendsStatus; //статус дружба между пользователями

    //список с информацией о том, что два пользователя являются друзьями
    private Set<Long> friends = new HashSet<>();

    public User() {
    }

    public User(long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}