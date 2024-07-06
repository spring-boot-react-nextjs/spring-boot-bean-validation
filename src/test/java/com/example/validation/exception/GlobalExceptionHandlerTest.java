package com.example.validation.exception;

import com.example.validation.i18n.I18nService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest
class GlobalExceptionHandlerTest {

    private static final String ERROR_URI = "https://example.com/error";
    private static final String ERROR_MESSAGE = "Test error message";
    private static final String I18N_MESSAGE = "Test error message (localized)";
    private static final int HTTP_STATUS_NOT_FOUND = 404;

    @Mock
    private I18nService i18nService;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private FieldError fieldError;

    @Mock
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        globalExceptionHandler.setErrorUri(ERROR_URI);
    }

    @Test
    void testHandleResourceNotFoundException() {
        ResourceNotFoundException ex = new ResourceNotFoundException(ERROR_MESSAGE, null);
        when(i18nService.getMessage(ERROR_MESSAGE, (String) null)).thenReturn(I18N_MESSAGE);

        ProblemDetail pd = globalExceptionHandler.handleResourceNotFoundException(ex);

        assertEquals(HTTP_STATUS_NOT_FOUND, pd.getStatus());
        assertEquals(I18N_MESSAGE, pd.getDetail());
        assertEquals(ERROR_URI, pd.getType().toString());
    }

    @Test
    void testHandleMethodArgumentNotValid() {
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        when(fieldError.getDefaultMessage()).thenReturn("Invalid field value");

        HttpHeaders headers = new HttpHeaders();
        HttpStatusCode status = HttpStatus.BAD_REQUEST;
        ResponseEntity<Object> response = globalExceptionHandler.handleMethodArgumentNotValid(
                methodArgumentNotValidException, headers, status, webRequest);

        assert response != null;
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).toString().contains("Invalid field value"));
    }
}