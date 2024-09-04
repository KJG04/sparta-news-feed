package com.sparta.spartanewsfeed.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AllExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException e) {

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", e.getReason());
        responseBody.put("status", e.getStatusCode().value());
        responseBody.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(e.getStatusCode()).body(responseBody);
    }
}
