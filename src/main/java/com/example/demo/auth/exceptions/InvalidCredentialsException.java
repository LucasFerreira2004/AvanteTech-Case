package com.example.demo.auth.exceptions;

import com.example.demo.shared.globalExceptions.ApplicationException;

public class InvalidCredentialsException extends ApplicationException {
    public InvalidCredentialsException(String field, String message) {
        super(field, message);
    }
    public InvalidCredentialsException(String field) {
        super(field, "credencias invalidas");
    }
}
