package ru.kuzmichev.SimpleBank.web.api.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.kuzmichev.SimpleBank.server.util.exception.ValidationException;
import ru.kuzmichev.SimpleBank.web.api.util.dto.response.ErrorResponse;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerController {
    private static final String VALIDATION_ERROR_CAUSE = "validation.error";
    private static final String GENERAL_ERROR_CAUSE = "general.error";

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponse handleException(MethodArgumentNotValidException e) {
        log.debug("invalid request", e);
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError.getField() + ": " + fieldError.getDefaultMessage();

        return buildErrorResponse(message, VALIDATION_ERROR_CAUSE);
    }

    @ExceptionHandler(value = ValidationException.class)
    @ResponseBody
    public ErrorResponse handleException(ValidationException e) {
        log.debug("invalid request", e);
        return buildErrorResponse(e.getMessage(), VALIDATION_ERROR_CAUSE);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ErrorResponse handleException(Exception e) {
        log.debug("unexpected error", e);
        return buildErrorResponse(e.toString(), GENERAL_ERROR_CAUSE);
    }

    private ErrorResponse buildErrorResponse(String errorMessage, String cause) {
        ErrorResponse response = new ErrorResponse()
                .setErrorMessage(errorMessage)
                .setCause(cause)
                .setError(true);
        log.debug("Error response: [{}]", response);
        return response;
    }
}
