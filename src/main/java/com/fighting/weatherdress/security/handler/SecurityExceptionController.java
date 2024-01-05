package com.fighting.weatherdress.security.handler;

import com.fighting.weatherdress.global.exception.CustomException;
import com.fighting.weatherdress.global.type.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SecurityExceptionController {

  @GetMapping("/exception/access-denied")
  public void accessDenied() {
    throw new CustomException(ErrorCode.ACCESS_DENIED);
  }

  @GetMapping("/exception/unauthorized")
  public void unauthorized() {
    throw new CustomException(ErrorCode.NEED_TO_SIGNIN);
  }
}
