package com.fighting.weatherdress.mail.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfiguration {

  @Value("${spring.mail.host}")
  private String host;

  @Value("${spring.mail.port}")
  private int port;

  @Value("${spring.mail.username}")
  private String username;

  @Value("${spring.mail.password}")
  private String password;

  @Value("${spring.mail.properties.mail.smtp.auth}")
  private boolean auth;

  @Value("${spring.mail.properties.mail.smtp.ssl.enable}")
  private boolean ssl;

  @Value("${spring.mail.properties.mail.smtp.ssl.trust}")
  private String sslTrust;

  @Bean
  public JavaMailSender javaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    Properties properties = new Properties();
    properties.put("mail.smtp.auth", auth);
    properties.put("mail.smtp.ssl.enable", ssl);
    properties.put("mail.smtp.ssl.trust", sslTrust);

    mailSender.setHost(host);
    mailSender.setPort(port);
    mailSender.setUsername(username);
    mailSender.setPassword(password);
    mailSender.setJavaMailProperties(properties);

    return mailSender;
  }

}
