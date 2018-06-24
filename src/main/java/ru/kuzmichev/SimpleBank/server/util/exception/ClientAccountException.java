package ru.kuzmichev.SimpleBank.server.util.exception;

public class ClientAccountException extends ValidationException {
    public ClientAccountException(String message) {
        super(message);
    }
}
