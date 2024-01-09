package com.fighting.weatherdress.member.service;

import com.fighting.weatherdress.global.exception.CustomException;
import com.fighting.weatherdress.global.type.ErrorCode;
import com.fighting.weatherdress.member.domain.Member;
import com.fighting.weatherdress.member.dto.SignInDto;
import com.fighting.weatherdress.member.repository.MemberRepository;
import com.fighting.weatherdress.security.dto.TokenResponse;
import com.fighting.weatherdress.security.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenProvider tokenProvider;

  // 소셜 로그인 사용자 제외한 일반 사용자 로그인 기능
  public TokenResponse signIn(SignInDto request) {
    Member member = memberRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMAIL));

    // 소셜 로그인으로 등록된 사용자는 password가 null이기 때문에 예외 처리
    if (member.getPassword() == null) {
      throw new CustomException(ErrorCode.REGISTERED_BY_SOCIAL);
    }

    if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
      throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
    }

    if (!member.isVerified()) {
      throw new CustomException(ErrorCode.NEED_TO_VERIFY_EMAIL);
    }

    return tokenProvider.generateTokenResponse(member.getEmail(), member.getRoles());
  }

  public void signOut(String accessToken, String email) {
    String token = tokenProvider.resolveTokenFromRequest(accessToken);
    tokenProvider.deleteRefreshToken(email);
    tokenProvider.addBlackList(token, email);
  }

  public TokenResponse reissueToken(String refreshToken) {
    return tokenProvider.regenerateAccessToken(refreshToken);
  }

}
