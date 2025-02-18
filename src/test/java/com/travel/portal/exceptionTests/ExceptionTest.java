package com.travel.portal.exceptionTests;
import com.travel.portal.exception.GlobalExceptionHandler;
import com.travel.portal.exception.exceptionDetails.ErrorResponse;
import com.travel.portal.exception.exceptionDetails.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class ExceptionTest {
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private WebRequest webRequest;

    @Mock
    private MethodParameter methodParameter;

    @BeforeEach
    void setUp() {
        openMocks(this);
        globalExceptionHandler = new GlobalExceptionHandler();
        when(webRequest.getDescription(false)).thenReturn("test-request");
    }

    @Test
    void handleUserNotFoundException_ShouldReturnNotFoundStatus() {
        // Arrange
        String errorMessage = "User Not Found";
        UserNotFoundException exception = new UserNotFoundException(errorMessage);

        // Act
        ResponseEntity<ErrorResponse> responseEntity =
                globalExceptionHandler.handleUserNotFoundException(exception, webRequest);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        ErrorResponse errorResponse = responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getStatus());
        assertEquals("User Not Found", errorResponse.getMessage());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void handleUserNotFoundException_ShouldReturnNotFoundStatusInt() {
        // Arrange
        UserNotFoundException exception = new UserNotFoundException(1);

        // Act
        ResponseEntity<ErrorResponse> responseEntity =
                globalExceptionHandler.handleUserNotFoundException(exception, webRequest);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        ErrorResponse errorResponse = responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getStatus());
        assertEquals("User not found with id: 1", errorResponse.getMessage());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void handleUserNotFoundException_ShouldIncludeTimestamp() {
        // Arrange
        UserNotFoundException exception = new UserNotFoundException("Test error");

        // Act
        ResponseEntity<ErrorResponse> responseEntity =
                globalExceptionHandler.handleUserNotFoundException(exception, webRequest);

        // Assert
        ErrorResponse errorResponse = responseEntity.getBody();
        assertNotNull(errorResponse);
        assertNotNull(errorResponse.getTimestamp());
        assertTrue(errorResponse.getTimestamp().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(errorResponse.getTimestamp().isAfter(LocalDateTime.now().minusSeconds(1)));
    }

    void handleUserNotFoundException_WithNullException_ShouldStillReturnErrorResponse() {
        // Act
        ResponseEntity<ErrorResponse> responseEntity =
                globalExceptionHandler.handleUserNotFoundException(null, webRequest);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        ErrorResponse errorResponse = responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getStatus());
    }

    @Test
    void handleMethodArgumentTypeMismatch_StringToInteger() {
        // Arrange
        String invalidValue = "abc";
        MethodArgumentTypeMismatchException exception = new MethodArgumentTypeMismatchException(
                invalidValue,
                Integer.class,
                "userId",
                methodParameter,
                new NumberFormatException("For input string: \"abc\"")
        );

        // Act
        ResponseEntity<ErrorResponse> responseEntity =
                globalExceptionHandler.handleMethodArgumentTypeMismatch(exception, webRequest);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        ErrorResponse errorResponse = responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatus());
        assertEquals("Failed to convert 'abc' to required type 'Integer'", errorResponse.getMessage());
        assertEquals("Invalid Parameter", errorResponse.getError());
        assertEquals("test-request", errorResponse.getPath());
    }

    @Test
    void handleGlobalException_WithRuntimeException() {
        // Arrange
        RuntimeException exception = new RuntimeException("Unexpected runtime error");

        // Act
        ResponseEntity<ErrorResponse> responseEntity =
                globalExceptionHandler.handleGlobalException(exception, webRequest);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        ErrorResponse errorResponse = responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getStatus());
        assertEquals("Unexpected runtime error", errorResponse.getMessage());
        assertEquals("Internal Server Error", errorResponse.getError());
        assertEquals("test-request", errorResponse.getPath());
    }
}
