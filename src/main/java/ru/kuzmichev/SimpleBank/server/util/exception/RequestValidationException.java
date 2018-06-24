package ru.kuzmichev.SimpleBank.server.util.exception;

public class RequestValidationException extends ValidationException {
    public RequestValidationException(String message) {
        super(message);
    }
}
