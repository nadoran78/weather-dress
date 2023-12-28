package com.fighting.weatherdress.global.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  // 공통
  INVALID_REQUEST("잘못된 요청입니다."),
  INTERNAL_SERVER_ERROR("내부 서버 오류가 발생했습니다."),

  // 회원가입
  ALREADY_EXIST_EMAIL("이미 존재하는 이메일입니다."),
  NOT_FOUND_EMAIL("존재하지 않는 이메일입니다."),
  INVALID_CODE("유효하지 않은 인증코드입니다.");

  private final String description;
}
