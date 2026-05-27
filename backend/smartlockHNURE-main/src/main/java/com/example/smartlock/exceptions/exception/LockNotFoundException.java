package com.example.smartlock.exceptions.exception;

public class LockNotFoundException extends RuntimeException {
    public LockNotFoundException(String message) {
        super(message);
    }
}
