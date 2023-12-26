package com.fighting.weatherdress.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class SignUpDto {

  @NotNull(message = "반드시 값이 있어야 합니다.")
  @Email(message = "유효한 이메일 주소가 아닙니다.")
  private String email;

  @NotNull(message = "반드시 값이 있어야 합니다.")
  @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}",
      message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
  private String password;

  @NotNull(message = "반드시 값이 있어야 합니다.")
  @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z-_]{2,10}$",
      message = "이름은 숫자, 특수문자를 제외한 2~10자리여야 합니다.")
  private String nickName;
}
