package com.example.validation.exception;

import com.example.validation.i18n.I18nService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${spring.application.error-uri}")
    private String errorUri;

    private final I18nService i18nService;

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ProblemDetail handleResourceNotFoundException(ResourceNotFoundException ex) {
        return getProblemDetail(
                HttpStatus.NOT_FOUND,
                i18nService.getMessage(
                        ex.getMessage(),
                        ex.getArgs()
                )
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        StringBuilder errorMessage = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                errorMessage.append(fieldError.getDefaultMessage()).append(";")
        );

        return ResponseEntity.badRequest().body(
                getProblemDetail(
                        HttpStatus.BAD_REQUEST,
                        errorMessage.toString())
        );
    }

    private ProblemDetail getProblemDetail(HttpStatus httpStatus, String detail) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                httpStatus,
                detail
        );
        pd.setType(URI.create(errorUri));
        return pd;
    }
}