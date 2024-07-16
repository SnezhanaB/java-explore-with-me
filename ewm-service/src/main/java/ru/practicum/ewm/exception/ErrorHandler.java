package ru.practicum.ewm.exception;

import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;


@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(NotFoundException e) {
        log.debug("[Status code 404 NOT_FOUND] {}", e.getMessage(), e);
        return ApiError.builder()
                .status(HttpStatus.NOT_FOUND.toString())
                .reason("The required object was not found.")
                .message(e.getMessage())
                .build();
    }


    @ExceptionHandler({MethodArgumentNotValidException.class, InvalidParametersException.class, MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handlerIncorrectParametersException(Exception e) {
        log.debug("[Status code 400 BAD_REQUEST] {}", e.getMessage(), e);
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .reason("Incorrectly made request.")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler({
            ConstraintViolationException.class,
            PSQLException.class,
            ConflictException.class,
            DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlerValidationException(Exception e) {
        log.debug("[Status code 409 CONFLICT] {}", e.getMessage());
        return ApiError.builder()
                .status(HttpStatus.CONFLICT.toString())
                .reason("Integrity constraint has been violated.")
                .message(e.getMessage())
                .build();
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handlerOtherException(Throwable e) {
        log.warn("[Status code 500 SERVER_ERROR] {}", e.getMessage(), e);
        return ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .reason("Internal Server Error")
                .message(e.getMessage())
                .build();
    }
}