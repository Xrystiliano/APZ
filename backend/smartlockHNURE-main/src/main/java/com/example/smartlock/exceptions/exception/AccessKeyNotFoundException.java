package com.example.smartlock.exceptions.exception;

public class AccessKeyNotFoundException extends RuntimeException {
    public AccessKeyNotFoundException(String message) {
        super(message);
    }
}
