package com.sparta.spartanewsfeed.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class PasswordErrorException extends RuntimeException {
    public PasswordErrorException(String message) { super(message); }
}
