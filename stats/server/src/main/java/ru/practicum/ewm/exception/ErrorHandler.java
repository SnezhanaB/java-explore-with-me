package ru.practicum.ewm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.controller.StatsController;

import java.util.Map;

@RestControllerAdvice(assignableTypes = {
        StatsController.class
})
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleDataValidationException(final DataValidationException e) {
        return Map.of("error", e.getMessage());
    }
}
