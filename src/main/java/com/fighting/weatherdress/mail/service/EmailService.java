package com.fighting.weatherdress.mail.service;

import com.fighting.weatherdress.global.util.RedisService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@RequiredArgsConstructor
@Service
public class EmailService {
  private final JavaMailSender mailSender;
  private final RedisService redisService;

  @Value("${spring.mail.username}")
  private String configEmail;

  // 인증코드 생성(0~9, a~z까지의 숫자와 문자를 섞어서 생성)
  private String generateCode() {
    int leftLimit = 48; // 아스키코드 0
    int rightLimit = 122; // 아스키코드 z
    int targetStringLength = 6;
    Random random = new Random();

    return random.ints(leftLimit, rightLimit + 1)
        .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
        .limit(targetStringLength)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
  }

  // thymeleaf 템플릿 html에 변수 주입
  private String setContext(String code) {
    Context context = new Context();
    TemplateEngine templateEngine = new TemplateEngine();
    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

    context.setVariable("code", code);

    templateResolver.setPrefix("templates/");
    templateResolver.setSuffix(".html");
    templateResolver.setTemplateMode(TemplateMode.HTML);
    templateResolver.setCacheable(false);

    templateEngine.setTemplateResolver(templateResolver);

    return templateEngine.process("mail", context);
  }

  // 이메일 양식 생성(인증코드 입력 및 레디스에 인증코드 저장 포함)
  private MimeMessage createEmailForm(String email) throws MessagingException {
    String authCode = generateCode();

    MimeMessage message = mailSender.createMimeMessage();
    message.addRecipients(RecipientType.TO, email);
    message.setSubject("기온별 옷차림 서비스의 인증번호입니다.");
    message.setFrom(configEmail);
    message.setText(setContext(authCode), "utf-8", "html");

    redisService.setDataExpire(email, authCode, 60 * 30L); // redis에 인증코드 저장(만료시간 30분)

    return message;
  }

  // 메일 전송 메서드
  public void sendEmail(String toEmail) throws MessagingException {
    if (redisService.existData(toEmail)) {
      redisService.deleteData(toEmail);
    }

    MimeMessage emailForm = createEmailForm(toEmail);

    mailSender.send(emailForm);
  }

  // 입력 인증코드 검증 메서드
  public Boolean verifiedEmail(String email, String code) {
    String savedCodeInRedis = redisService.getData(email);
    if (savedCodeInRedis == null) {
      return false;
    }
    return savedCodeInRedis.equals(code);
  }
}
