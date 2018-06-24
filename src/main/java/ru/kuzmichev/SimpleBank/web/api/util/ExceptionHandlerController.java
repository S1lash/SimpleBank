package ru.kuzmichev.SimpleBank.web.api.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.kuzmichev.SimpleBank.server.util.exception.ValidationException;
import ru.kuzmichev.SimpleBank.web.api.util.dto.response.ErrorResponse;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerController {
    private static final String VALIDATION_ERROR_CAUSE = "validation.error";
    private static final String GENERAL_ERROR_CAUSE = "general.error";

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponse handleException(MethodArgumentNotValidException e, HttpServletResponse httpResponse) {
        log.debug("invalid request", e);
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError.getField() + ": " + fieldError.getDefaultMessage();

        return buildErrorResponse(httpResponse, message, VALIDATION_ERROR_CAUSE);
    }

    @ExceptionHandler(value = ValidationException.class)
    @ResponseBody
    public ErrorResponse handleException(ValidationException e, HttpServletResponse httpResponse) {
        log.debug("invalid request", e);
        return buildErrorResponse(httpResponse, e.getMessage(), VALIDATION_ERROR_CAUSE);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ErrorResponse handleException(Exception e, HttpServletResponse httpResponse) {
        log.debug("unexpected error", e);
        return buildErrorResponse(httpResponse, e.toString(), GENERAL_ERROR_CAUSE);
    }

    private ErrorResponse buildErrorResponse(HttpServletResponse httpResponse, String errorMessage, String cause) {
        httpResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        ErrorResponse response = new ErrorResponse()
                .setErrorMessage(errorMessage)
                .setCause(cause)
                .setError(true);
        log.debug("Error response: [{}]", response);
        return response;
    }
}
