package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validation.UserValidate;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //получение пользователя по id
    @GetMapping("/{id}")
    public User getUserById(
            @PathVariable("id") long id) {
        log.debug("Получен запрос Get /users/{id} на получение пользователя по id: {}", id);
        if (id <= 0) {
            throw new IncorrectParameterException("id");
        }
        return userService.getUserById(id);
    }

    //получение списка всех пользователей
    @GetMapping
    public Collection<User> getAllUsers() {
        log.debug("Получен запрос Get /users, количество пользователей: {}", userService.getAllUsers().size());
        return userService.getAllUsers();
    }

    //создание пользователя
    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.debug("Получен запрос Post /users на добавлен пользователя: {}", user);
        if (UserValidate.userValidate(user)) {
            return userService.createUser(user);
        }
        return user;
    }

    //обновление пользователя
    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.debug("Получен запрос Put /users на обновление пользователя: {}", user);
        return userService.updateUser(user);
    }

    //добавление в друзья
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(
            @PathVariable("id") long id,
            @PathVariable("friendId") long friendId) {
        log.debug("Получен запрос Put /users/{id}/friends/{friendId} " +
                        "на добавление пользователя с Id: {} в друзья пользователю с Id: {}",
                friendId, id);
        userService.addFriend(id, friendId);
    }

    //удаление из друзей
    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(
            @PathVariable("id") long id,
            @PathVariable("friendId") long friendId) {
        log.debug("Получен запрос Delete /users/{id}/friends/{friendId} " +
                        "на удаление пользователя с Id: {} из друзей пользователя с Id: {}",
                id, friendId);
        userService.deleteFriend(id, friendId);
    }

    //возвращаем список пользователей, являющихся его друзьями
    @GetMapping("/{id}/friends")
    public Collection<User> getUserFriendsById(@PathVariable("id") long id) {
        log.debug("Получен запрос Get /users/{id}/friends " +
                "на получение списка друзей пользователя с Id: {}", id);
        return userService.getUserFriendsById(id);
    }

    //список друзей, общих с другим пользователем
    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(
            @PathVariable("id") long id,
            @PathVariable("otherId") long otherId) {
        log.debug("Получен запрос Get /users/{id}/friends/common/{otherId} " +
                        "на получение списка общих друзей пользователей с Id: {} и Id: {}",
                id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}