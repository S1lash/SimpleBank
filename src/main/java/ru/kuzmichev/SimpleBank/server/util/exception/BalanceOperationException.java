package ru.kuzmichev.SimpleBank.server.util.exception;

public class BalanceOperationException extends Exception {
    public BalanceOperationException(String message) {
        super(message);
    }
}
