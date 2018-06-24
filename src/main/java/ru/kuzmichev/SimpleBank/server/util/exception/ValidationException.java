package ru.kuzmichev.SimpleBank.server.util.exception;

public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}
