package com.fighting.weatherdress.mail.service;

import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// 일회성 테스트로 disable 처리함
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
    String code = "szmvFJ";
    //when
    Boolean b = emailService.verifiedEmail(email, code);
    //then
    System.out.println(b);

  }
}