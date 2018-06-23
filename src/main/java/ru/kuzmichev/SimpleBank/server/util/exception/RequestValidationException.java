package ru.kuzmichev.SimpleBank.server.util.exception;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RequestValidationException extends Exception {

    public RequestValidationException(String message) {
        super(message);
    }
}
