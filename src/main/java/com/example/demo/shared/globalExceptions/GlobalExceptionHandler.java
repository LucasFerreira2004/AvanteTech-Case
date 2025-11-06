package com.example.demo.shared.globalExceptions;

import com.example.demo.auth.exceptions.InvalidCredentialsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponseDTO>> handleValidationException(MethodArgumentNotValidException e) {
        List<ErrorResponseDTO> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ErrorResponseDTO(
                        error.getField(),
                        error.getDefaultMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase()
                ))
                .toList();

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException e) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                e.getField(),
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidCredentialsException(InvalidCredentialsException e) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                e.getField(),
                e.getMessage(),
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

}

