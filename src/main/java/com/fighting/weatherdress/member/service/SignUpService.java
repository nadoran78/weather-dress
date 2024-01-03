package com.fighting.weatherdress.member.service;

import com.fighting.weatherdress.global.exception.CustomException;
import com.fighting.weatherdress.global.type.CommonResponse;
import com.fighting.weatherdress.global.type.ErrorCode;
import com.fighting.weatherdress.mail.service.EmailService;
import com.fighting.weatherdress.member.domain.Member;
import com.fighting.weatherdress.member.dto.SignUpDto;
import com.fighting.weatherdress.member.repository.MemberRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignUpService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final EmailService emailService;

  @Transactional
  public void signUp(SignUpDto request) throws MessagingException {
    if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
      throw new CustomException(ErrorCode.ALREADY_EXIST_EMAIL);
    }

    request.setPassword(passwordEncoder.encode(request.getPassword()));

    Member savedMember = memberRepository.save(Member.fromDto(request));

    emailService.sendEmail(savedMember.getEmail());

  }

  // 이메일 인증
  @Transactional
  public void verifyEmail(String email, String code) {
    if (emailService.verifiedEmail(email, code)) {
      Member savedMember = memberRepository.findByEmail(email)
          .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMAIL));
      savedMember.setVerified(true);
      memberRepository.save(savedMember);
    } else {
      throw new CustomException(ErrorCode.INVALID_CODE);
    }
  }

}
