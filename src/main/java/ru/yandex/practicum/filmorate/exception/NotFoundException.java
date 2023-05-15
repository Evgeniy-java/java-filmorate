package ru.yandex.practicum.filmorate.exception;

public class NotFoundException extends ValidationException {
    public NotFoundException(String s) {
        super(s);
    }
}