package com.example.demo.shared.globalExceptions;

public record ErrorResponseDTO(String field, String message, Integer statusCode, String error) {
}

