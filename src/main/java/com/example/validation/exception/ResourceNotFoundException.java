package com.example.validation.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {
    private final String message;
    private final String[] args;

    public ResourceNotFoundException(String message, String... args) {
        this.message = message;
        this.args = args;
    }
}