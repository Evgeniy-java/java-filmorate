package ru.yandex.practicum.filmorate.exception;

public class UserNotFoundException extends IncorrectParameterException{

    public UserNotFoundException(String parameter) {
        super(parameter);
    }
}
