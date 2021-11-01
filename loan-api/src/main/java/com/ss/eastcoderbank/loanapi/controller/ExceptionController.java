package com.ss.eastcoderbank.loanapi.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.ss.eastcoderbank.core.exeception.AccountNotEmptyException;
import com.ss.eastcoderbank.core.exeception.AccountNotFoundException;
import com.ss.eastcoderbank.core.exeception.UserNotFoundException;
import com.ss.eastcoderbank.core.exeception.response.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> userValidationError(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        errors.put("status", HttpStatus.BAD_REQUEST.toString());
        errors.put("message", "");
        exception.getFieldErrors().forEach(fieldError -> {
            String fieldName = fieldError.getField();
            String errorMessage = fieldError.getDefaultMessage();
            errors.put(fieldName, errorMessage);
            errors.put("message", errors.get("message") + " " + errorMessage);
        });
        log.warn(errors.toString());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> forbidden(AccessDeniedException exception) {
        log.warn("Access is forbidden, user does not have permission to access this resource.");
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.FORBIDDEN.toString(), exception.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<ErrorMessage> jsonParseFailure(JsonParseException exception) {
        log.warn("JSON is not valid.");
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.BAD_REQUEST.toString(), "Not valid json. " +  exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> noUserFound(UserNotFoundException exception) {
        log.warn("User is not found.");
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.NOT_FOUND.toString(), exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorMessage> duplicateConstraints(AccountNotFoundException exception) {
        log.warn("Account is not found.");
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.NOT_FOUND.toString(), exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountNotEmptyException.class)
    public ResponseEntity<ErrorMessage> duplicateConstraints(AccountNotEmptyException exception) {
        log.warn("Account is not empty.");
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.PRECONDITION_FAILED.toString(), exception.getMessage()), HttpStatus.PRECONDITION_FAILED);
    }
}
