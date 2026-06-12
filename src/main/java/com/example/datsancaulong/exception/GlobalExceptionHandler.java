package com.example.datsancaulong.exception;

import com.example.datsancaulong.dto.response.ApiDataResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiDataResponse<?>> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                false,
                exception.getMessage(),
                null,
                null,
                HttpStatus.NOT_FOUND
        ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiDataResponse<?>> handleNotFoundException(NotFoundException exception) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                false,
                exception.getMessage(),
                null,
                null,
                HttpStatus.NOT_FOUND
        ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RefreshTokenInvalidException.class)
    public ResponseEntity<ApiDataResponse<?>> handleRefreshTokenInvalidException(RefreshTokenInvalidException exception) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                false,
                exception.getMessage(),
                null,
                null,
                HttpStatus.NOT_FOUND
        ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiDataResponse<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return new ResponseEntity<>(new ApiDataResponse<>(
                false,
                "Lỗi đầu vào",
                null,
                errors,
                HttpStatus.BAD_REQUEST
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ImageExtentionInvalidExeption.class)
    public ResponseEntity<ApiDataResponse<?>> handleImageExtentionInvalidExeption(ImageExtentionInvalidExeption exception) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                false,
                exception.getMessage(),
                null,
                null,
                HttpStatus.NOT_FOUND
        ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiDataResponse<?>> handleBadRequest(BadRequestException exception) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                false,
                exception.getMessage(),
                null,
                null,
                HttpStatus.BAD_REQUEST
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiDataResponse<?>> handleUnauthorized(UnauthorizedException exception) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                false,
                exception.getMessage(),
                null,
                null,
                HttpStatus.UNAUTHORIZED
        ), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ApiDataResponse<?>> handleInternalServer(InternalServerException exception) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                false,
                exception.getMessage(),
                null,
                null,
                HttpStatus.INTERNAL_SERVER_ERROR
        ), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiDataResponse<?>> handleAll(Exception exception) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                false,
                exception.getMessage(),
                null,
                null,
                HttpStatus.INTERNAL_SERVER_ERROR
        ), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
