package com.fighting.weatherdress.global.exception;

import com.fighting.weatherdress.global.type.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomException extends RuntimeException {

  private ErrorCode errorCode;
  private HttpStatus status;
  private String errorMessage;

  public CustomException(ErrorCode errorCode) {
    this.errorCode = errorCode;
    this.status = errorCode.getStatus();
    this.errorMessage = errorCode.getDescription();
  }
}
