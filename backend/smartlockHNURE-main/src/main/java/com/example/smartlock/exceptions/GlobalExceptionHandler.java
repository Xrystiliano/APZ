package com.example.smartlock.exceptions;

import com.example.smartlock.exceptions.exception.AccessKeyNotFoundException;
import com.example.smartlock.exceptions.exception.LockNotFoundException;
import com.example.smartlock.exceptions.exception.UserNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        ErrorResponse error = new ErrorResponse(e.getMessage(), "USER_NOT_FOUND");

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LockNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLockNotFoundException(LockNotFoundException e) {
        ErrorResponse error = new ErrorResponse(e.getMessage(), "LOCK_NOT_FOUND");

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(AccessKeyNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccessKeyNotFoundException(AccessKeyNotFoundException e) {
        ErrorResponse error = new ErrorResponse(e.getMessage(), "ACCESS_KEY_NOT_FOUND");

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        System.out.println("DEBUG: " + e.getMessage());

        ErrorResponse error = new ErrorResponse(e.getMessage(), "DATA_INTEGRITY_VIOLATION");

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException e) {
        System.out.println("DEBUG: " + e.getMessage());

        ErrorResponse error = new ErrorResponse(e.getMessage(), "DATA_ACCESS_ERROR");

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
