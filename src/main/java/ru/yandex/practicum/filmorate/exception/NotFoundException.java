package ru.yandex.practicum.filmorate.exception;

public class NotFoundException extends IncorrectParameterException{

    public NotFoundException(String parameter) {
        super(parameter);
    }
}
