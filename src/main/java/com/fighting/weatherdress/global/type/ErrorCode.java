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
  INVALID_CODE("유효하지 않은 인증코드입니다."),

  // 토큰 관련
  INVALID_TOKEN("토큰 정보가 유효하지 않습니다."),
  NOT_FOUND_REFRESH_TOKEN("토큰정보가 저장되어 있지 않습니다."),
  UNMATCHED_SAVED_REFRESH_TOKEN("저장된 토큰과 일치하지 않습니다."),
  ACCESS_DENIED("접근 권한이 없습니다."),
  NEED_TO_SIGNIN("로그인을 해야 합니다.");

  private final String description;
}
