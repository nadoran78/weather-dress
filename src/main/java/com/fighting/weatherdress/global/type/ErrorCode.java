package com.fighting.weatherdress.global.type;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  // 공통
  INVALID_REQUEST(BAD_REQUEST, "잘못된 요청입니다."),
  SERVER_ERROR(INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생했습니다."),

  // 회원가입 및 로그인
  ALREADY_EXIST_EMAIL(BAD_REQUEST, "이미 존재하는 이메일입니다."),
  NOT_FOUND_EMAIL(NOT_FOUND, "존재하지 않는 이메일입니다."),
  INVALID_CODE(BAD_REQUEST, "유효하지 않은 인증코드입니다."),
  INCORRECT_PASSWORD(BAD_REQUEST, "패스워드가 일치하지 않습니다."),
  REGISTERED_BY_SOCIAL(BAD_REQUEST, "소셜로그인을 통해 등록된 사용자입니다."),
  NEED_TO_VERIFY_EMAIL(BAD_REQUEST, "이메일 인증이 필요합니다."),

  // 토큰 관련
  INVALID_TOKEN(FORBIDDEN, "토큰 정보가 유효하지 않습니다."),
  NOT_FOUND_REFRESH_TOKEN(FORBIDDEN, "토큰정보가 저장되어 있지 않습니다."),
  UNMATCHED_SAVED_REFRESH_TOKEN(FORBIDDEN, "저장된 토큰과 일치하지 않습니다."),
  ACCESS_DENIED(FORBIDDEN, "접근 권한이 없습니다."),
  NEED_TO_SIGNIN(FORBIDDEN, "로그인을 해야 합니다."),

  // 단기예보 관련
  NOT_FOUND_LOCATION(NOT_FOUND, "일치하는 지역이 없습니다."),

  // S3 관련
  FAIL_TO_UPLOAD_FILE(INTERNAL_SERVER_ERROR, "파일 업로드가 실패하였습니다."),

  MEMBER_NOT_FOUND(NOT_FOUND, "가입한 멤버가 존재하지 않습니다."),
  POST_NOT_FOUND(NOT_FOUND, "작성된 게시글이 존재하지 않습니다."),
  MEMBER_IS_NOT_WRITER(FORBIDDEN, "작성자가 아닙니다."),
  NOT_FOUND_REPLY(NOT_FOUND, "해당 답글이 존재하지 않습니다.")

  ;

  private final HttpStatus status;
  private final String description;
}
