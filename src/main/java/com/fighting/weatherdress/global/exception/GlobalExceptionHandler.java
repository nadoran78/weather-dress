package com.fighting.weatherdress.global.exception;

import com.fighting.weatherdress.global.dto.CustomErrorResponse;
import com.fighting.weatherdress.global.type.ErrorCode;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    log.error("MethodArgumentNotValidException is occurred.", e);

    Map<String, String> errors = new HashMap<>();
    e.getBindingResult().getAllErrors()
        .forEach(error -> errors.put(((FieldError) error).getField(), error.getDefaultMessage()));

    return ResponseEntity.badRequest().body(errors);
  }

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<CustomErrorResponse> handleCustomException(CustomException e) {
    log.error("{} is occurred.", e.getErrorCode());
    return ResponseEntity.status(e.getStatus()).body(
        CustomErrorResponse.builder()
            .errorCode(e.getErrorCode())
            .errorMessage(e.getErrorMessage())
            .build());
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<CustomErrorResponse> handleDataIntegrityViolationException(
      DataIntegrityViolationException e) {
    log.error("DataIntegrityViolationException is occurred.", e);

    return ResponseEntity.badRequest().body(
        CustomErrorResponse.builder()
            .errorCode(ErrorCode.INVALID_REQUEST)
            .errorMessage(ErrorCode.INVALID_REQUEST.getDescription())
            .build());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<CustomErrorResponse> handleException(Exception e) {
    log.error("Exception is occurred.", e);

    return ResponseEntity.status(500).body(
        CustomErrorResponse.builder()
            .errorCode(ErrorCode.INTERNAL_SERVER_ERROR)
            .errorMessage(ErrorCode.INTERNAL_SERVER_ERROR.getDescription())
            .build());
  }
}
