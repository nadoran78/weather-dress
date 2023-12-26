package com.fighting.weatherdress.member.service;

import com.fighting.weatherdress.global.exception.CustomException;
import com.fighting.weatherdress.global.type.CommonResponse;
import com.fighting.weatherdress.global.type.ErrorCode;
import com.fighting.weatherdress.member.domain.Member;
import com.fighting.weatherdress.member.dto.CustomUserDetails;
import com.fighting.weatherdress.member.dto.SignUpDto;
import com.fighting.weatherdress.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return new CustomUserDetails(memberRepository.getByEmail(username));
  }

  public CommonResponse signUp(SignUpDto request) {
    if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
      throw new CustomException(ErrorCode.ALREADY_EXIST_EMAIL);
    }

    request.setPassword(passwordEncoder.encode(request.getPassword()));

    memberRepository.save(Member.fromDto(request));

    return CommonResponse.SUCCESS;
  }

}
