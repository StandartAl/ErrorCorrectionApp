package com.project.errorcorrection.exception;

import com.project.errorcorrection.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Глобальный обработчик ошибок REST-контроллеров.
 * Преобразует исключения приложения в единую структуру ответа {@link ErrorResponse} с заданными кодами ошибок.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    /**
     * Обработка ошибки 404 - Задача не найдена (код 40401).
     */
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFound(TaskNotFoundException ex, HttpServletRequest request) {
        log.warn("Task not found: {}", ex.getMessage());
        ErrorResponse response = ErrorResponse.builder()
                .errorMessage(ex.getMessage())
                .errorCode(40401)
                .timestamp(LocalDateTime.now().format(TIMESTAMP_FORMATTER))
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Обработка ошибок валидации тела запроса (код 40001).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");
        log.warn("Validation error: {}", message);
        ErrorResponse response = ErrorResponse.builder()
                .errorMessage(message)
                .errorCode(40001)
                .timestamp(LocalDateTime.now().format(TIMESTAMP_FORMATTER))
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Обработка нечитаемого/некорректного JSON или значения Enum (код 40001).
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        String message = "Invalid JSON or parameter format (check language RU/EN and request body)";
        log.warn("HTTP message not readable: {}", ex.getMessage());
        ErrorResponse response = ErrorResponse.builder()
                .errorMessage(message)
                .errorCode(40001)
                .timestamp(LocalDateTime.now().format(TIMESTAMP_FORMATTER))
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Обработка прочих неперехваченных исключений (код 50001).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception processing request: {}", request.getRequestURI(), ex);
        ErrorResponse response = ErrorResponse.builder()
                .errorMessage("Internal server error: " + ex.getMessage())
                .errorCode(50001)
                .timestamp(LocalDateTime.now().format(TIMESTAMP_FORMATTER))
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
