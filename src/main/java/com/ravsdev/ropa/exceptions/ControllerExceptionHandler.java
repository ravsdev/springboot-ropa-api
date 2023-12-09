package com.ravsdev.ropa.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage notValidExceptionHandler(MethodArgumentNotValidException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                ex.getBindingResult().getFieldErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .findFirst()
                        .orElse(ex.getMessage()));
        return message;
    }
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage notFoundExceptionHandler(NotFoundException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage());

        return message;
    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessage alreadyExistsExceptionHandler(AlreadyExistsException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.CONFLICT.value(),
                ex.getMessage());
        return message;
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage messageNotReadableHandler(HttpMessageNotReadableException ex){
        final Pattern ENUM_MSG = Pattern.compile("\\[.+\\]");

        if (ex.getCause() != null && ex.getCause() instanceof InvalidFormatException) {
            Matcher match = ENUM_MSG.matcher(ex.getCause().getMessage());
            if (match.find()) {
                return new ErrorMessage(HttpStatus.BAD_REQUEST.value(),"Categorías válidas: " + match.group(0));
            }
        }

        ErrorMessage message = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage());

        return message;
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage globalExceptionHandler(Exception ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage());

        return message;
    }
}