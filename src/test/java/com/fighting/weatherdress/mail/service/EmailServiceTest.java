package com.fighting.weatherdress.mail.service;

import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

// 일회성 테스트로 disable 처리함
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Disabled
class EmailServiceTest {

  @Autowired
  private EmailService emailService;

  @Test
  @DisplayName("이메일 발송 테스트")
  void successSendEmail() throws MessagingException {
    //given
    String toEmail = "nadoran78@gmail.com";
    //when
    emailService.sendEmail(toEmail);
  }

  @Test
  @DisplayName("이메일에 전송된 인증코드를 사용한 인증 테스트")
  void successVerifiedEmail() {
    //given
    String email = "nadoran78@gmail.com";
    String code = "Xy1xWU";
    //when
    Boolean result = emailService.verifiedEmail(email, code);
    //then
    Assertions.assertEquals(result, true);
  }

  @Test
  @DisplayName("인증코드 불일치 시 실패 테스트")
  void failVerifiedEmail() {
    // given
    String email = "nadoran78@gmail.com";
    String code = "1111";
    // when
    Boolean result = emailService.verifiedEmail(email, code);
    // then
    Assertions.assertEquals(result, false);
  }
}