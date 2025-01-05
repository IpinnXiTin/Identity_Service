package com.ipinxitin.identity_service.exception;

import java.util.Map;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ipinxitin.identity_service.dto.response.ApiResponse;

import jakarta.validation.ConstraintViolation;

@ControllerAdvice
public class GlobalExceptionHandling {

    Map<String, Object> attrs;

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse> handleAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();

        ApiResponse<?> apiResponse = ApiResponse.builder()
            .code(errorCode.getCode())
            .message(errorCode.getMessage())
            .build();

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf(enumKey);

        var constraintViolation = exception.getBindingResult()
            .getAllErrors().getFirst().unwrap(ConstraintViolation.class);

        attrs = constraintViolation.getConstraintDescriptor().getAttributes();

        ApiResponse<?> apiResponse = ApiResponse.builder()
            .code(errorCode.getCode())
            .message(Objects.nonNull(attrs) 
                ? errorCode.getMessage().replace("{" + "min" + "}", String.valueOf(attrs.get("min")))
                : errorCode.getMessage())
            .build();

        return ResponseEntity.badRequest().body(apiResponse);
    }
}
