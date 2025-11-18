package com.project.expenseTracker.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(ExpenseNotFoundException.class)
    public Map<String, String> handleExpenseNotFountException(ExpenseNotFoundException ex) {
        log.error("ExpenseNotFoundException occurred");
        Map<String, String> map = new HashMap<>();
        map.put("message", ex.getMessage());
        map.put("timestamp", LocalDateTime.now().toString());
        return map;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(UnauthorizedException.class)
    public Map<String, String> handleExpenseNotFountException(UnauthorizedException ex) {
        log.error("UnauthorizedException occurred");
        Map<String, String> map = new HashMap<>();
        map.put("message", ex.getMessage());
        map.put("timestamp", LocalDateTime.now().toString());
        return map;
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException occurred");
        Map<String, String> map = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> map.put(error.getField(), error.getDefaultMessage()));
        return map;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Map<String, String> handleMethodArgumentNotValidException(MethodArgumentTypeMismatchException ex) {
        log.error("MethodArgumentTypeMismatchException occurred");
        Map<String, String> map = new HashMap<>();
        map.put("message", ex.getErrorCode());
        return map;
    }
}
