package com.demo.valid.web;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * 把校验失败转成可读 JSON。
 * <ul>
 *   <li>{@link MethodArgumentNotValidException} — Controller 里 @RequestBody + @Valid/@Validated 失败</li>
 *   <li>{@link ConstraintViolationException} — 方法参数校验 / 手动 validate() 失败</li>
 * </ul>
 */
@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<ApiError.FieldErrorDetail> details = ex.getBindingResult().getFieldErrors().stream()
                .map(this::toDetail)
                .toList();
        return ResponseEntity.badRequest().body(new ApiError("请求体校验失败", details));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex) {
        List<ApiError.FieldErrorDetail> details = ex.getConstraintViolations().stream()
                .map(this::toDetail)
                .toList();
        return ResponseEntity.badRequest().body(new ApiError("约束校验失败", details));
    }

    private ApiError.FieldErrorDetail toDetail(FieldError error) {
        Object rejected = error.getRejectedValue();
        return new ApiError.FieldErrorDetail(
                error.getField(),
                rejected == null ? null : String.valueOf(rejected),
                error.getDefaultMessage());
    }

    private ApiError.FieldErrorDetail toDetail(ConstraintViolation<?> violation) {
        Object rejected = violation.getInvalidValue();
        return new ApiError.FieldErrorDetail(
                violation.getPropertyPath().toString(),
                rejected == null ? null : String.valueOf(rejected),
                violation.getMessage());
    }
}
