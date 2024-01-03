package com.fighting.weatherdress.mail.config;

import java.util.Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@RequiredArgsConstructor
public class EmailConfiguration {

  private final EmailProperties emailProperties;

  @Bean
  public JavaMailSender javaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    Properties properties = new Properties();
    properties.put("mail.smtp.auth", emailProperties.isAuth());
    properties.put("mail.smtp.ssl.enable", emailProperties.isSsl());
    properties.put("mail.smtp.ssl.trust", emailProperties.getSslTrust());

    mailSender.setHost(emailProperties.getHost());
    mailSender.setPort(emailProperties.getPort());
    mailSender.setUsername(emailProperties.getUsername());
    mailSender.setPassword(emailProperties.getPassword());
    mailSender.setJavaMailProperties(properties);

    return mailSender;
  }

}
