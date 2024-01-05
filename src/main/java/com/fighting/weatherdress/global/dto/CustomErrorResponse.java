package com.fighting.weatherdress.global.dto;

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
public class CustomErrorResponse {
  private ErrorCode errorCode;
  private String errorMessage;

}
