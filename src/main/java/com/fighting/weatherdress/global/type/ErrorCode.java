package com.fighting.weatherdress.global.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  // 공통
  INVALID_REQUEST(400, "잘못된 요청입니다."),
  INTERNAL_SERVER_ERROR(500, "내부 서버 오류가 발생했습니다."),

  // 회원가입 및 로그인
  ALREADY_EXIST_EMAIL(400, "이미 존재하는 이메일입니다."),
  NOT_FOUND_EMAIL(400, "존재하지 않는 이메일입니다."),
  INVALID_CODE(400, "유효하지 않은 인증코드입니다."),
  INCORRECT_PASSWORD(400, "패스워드가 일치하지 않습니다."),
  REGISTERED_BY_SOCIAL(400, "소셜로그인을 통해 등록된 사용자입니다."),
  NEED_TO_VERIFY_EMAIL(400, "이메일 인증이 필요합니다."),

  // 토큰 관련
  INVALID_TOKEN(400, "토큰 정보가 유효하지 않습니다."),
  NOT_FOUND_REFRESH_TOKEN(500, "토큰정보가 저장되어 있지 않습니다."),
  UNMATCHED_SAVED_REFRESH_TOKEN(500, "저장된 토큰과 일치하지 않습니다."),
  ACCESS_DENIED(400, "접근 권한이 없습니다."),
  NEED_TO_SIGNIN(400, "로그인을 해야 합니다."),

  // 단기예보 관련
  NOT_FOUND_LOCATION(500, "일치하는 지역이 없습니다."),

  // S3 관련
  FAIL_TO_UPLOAD_FILE(500, "파일 업로드가 실패하였습니다.");

  private final int status;
  private final String description;
}
