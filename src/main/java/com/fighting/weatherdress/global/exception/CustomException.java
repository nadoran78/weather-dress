package com.fighting.weatherdress.global.exception;

import com.fighting.weatherdress.global.type.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomException extends RuntimeException {

  private ErrorCode errorCode;
  private String errorMessage;

  public CustomException(ErrorCode errorCode) {
    this.errorCode = errorCode;
    this.errorMessage = errorCode.getDescription();
  }
}
