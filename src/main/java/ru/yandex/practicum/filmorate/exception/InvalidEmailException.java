package ru.yandex.practicum.filmorate.exception;

public class InvalidEmailException extends Throwable { 
    public InvalidEmailException(String s) {
        super(s);
    }
}