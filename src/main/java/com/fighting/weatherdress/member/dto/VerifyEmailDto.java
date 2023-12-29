package com.fighting.weatherdress.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyEmailDto {
  @NotNull(message = "반드시 값이 있어야 합니다.")
  @Email(message = "유효한 이메일 주소가 아닙니다.")
  private String email;

  @NotNull(message = "반드시 값이 있어야 합니다.")
  private String code;
}
