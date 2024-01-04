package com.fighting.weatherdress.security.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class SecurityConfigurationTest {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  @DisplayName("비밀번호 매치 성공 테스트")
  void passwordEncoder() {
    String encoded = passwordEncoder.encode("abc");
    boolean result = passwordEncoder.matches("abc", encoded);
    Assertions.assertTrue(result);
  }
}