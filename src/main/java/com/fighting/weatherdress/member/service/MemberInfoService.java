package com.fighting.weatherdress.member.service;

import com.fighting.weatherdress.global.exception.CustomException;
import com.fighting.weatherdress.global.type.ErrorCode;
import com.fighting.weatherdress.mail.service.EmailService;
import com.fighting.weatherdress.member.domain.Member;
import com.fighting.weatherdress.member.dto.ChangePasswordDto;
import com.fighting.weatherdress.member.repository.MemberRepository;
import com.fighting.weatherdress.security.dto.MemberInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberInfoService {

  private final MemberRepository memberRepository;
  private final EmailService emailService;
  private final PasswordEncoder passwordEncoder;

  // 이메일 인증코드 확인 후 패스워드 변현
  @Transactional
  public void changePassword(String email, ChangePasswordDto request) {
    if (emailService.verifiedEmail(email, request.getCode())) {
      Member savedMember = memberRepository.findByEmail(email)
          .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMAIL));
      savedMember.setPassword(passwordEncoder.encode(request.getNewPassword()));
      memberRepository.save(savedMember);
    } else {
      throw new CustomException(ErrorCode.INVALID_CODE);
    }
  }

  public MemberInfoDto getMemberInfo(String email) {
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMAIL));
    return MemberInfoDto.fromEntity(member);
  }
}
