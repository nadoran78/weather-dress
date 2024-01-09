package com.fighting.weatherdress.member.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fighting.weatherdress.global.exception.CustomException;
import com.fighting.weatherdress.global.type.ErrorCode;
import com.fighting.weatherdress.mail.service.EmailService;
import com.fighting.weatherdress.member.domain.Member;
import com.fighting.weatherdress.member.dto.SignUpDto;
import com.fighting.weatherdress.member.repository.MemberRepository;
import jakarta.mail.MessagingException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class SignUpServiceTest {

  @Mock
  private MemberRepository memberRepository;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private EmailService emailService;
  @InjectMocks
  private SignUpService signUpService;

  @Test
  @DisplayName("회원가입 성공 테스트")
  void successSignUp() throws MessagingException {
    //given
    Member member = Member.builder()
        .email("abc@abcd.com")
        .build();

    given(memberRepository.findByEmail(anyString())).willReturn(Optional.empty());
    given(passwordEncoder.encode(anyString())).willReturn("abcd");
    given(memberRepository.save(any(Member.class))).willReturn(member);

    //when
    SignUpDto request = SignUpDto.builder()
        .email("abc@abcd.com")
        .password("1234")
        .nickName("아리아리")
        .build();
    signUpService.signUp(request);
    //then
    verify(memberRepository, times(1)).save(any(Member.class));
  }

  @Test
  @DisplayName("회원가입 시 이미 존재하는 이메일일 경우 예외처리 테스트")
  void signUp_AlreadyExistEmail() {
    //given
    Member member = Member.builder()
        .email("abc@abc.com")
        .build();
    given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));

    //when
    SignUpDto request = SignUpDto.builder()
        .email("abc@abcd.com")
        .password("1234")
        .nickName("아리아리")
        .build();
    CustomException customException = assertThrows(CustomException.class,
        () -> signUpService.signUp(request));
    //then
    assertEquals(customException.getErrorCode(), ErrorCode.ALREADY_EXIST_EMAIL);
  }

  @Test
  @DisplayName("인증코드를 사용한 이메일 인증 성공 테스트")
  void successVerifyEmail() {
    //given
    Member member = Member.builder()
        .email("abc@abcd.com")
        .build();
    given(emailService.verifiedEmail(anyString(), anyString())).willReturn(true);
    given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));
    given(memberRepository.save(any(Member.class))).will(returnsFirstArg());
    //when
    String email = "abc@abcd.com";
    String code = "abcd";
    signUpService.verifyEmail(email, code);
    //then
    verify(memberRepository, times(1)).save(any(Member.class));
  }

  @Test
  @DisplayName("이메일 인증 시 db에 저장된 이메일 없을 경우 예외 처리 테스트")
  void verifyEmail_NotFoundEmail() {
    //given
    given(emailService.verifiedEmail(anyString(), anyString())).willReturn(true);
    given(memberRepository.findByEmail(anyString())).willReturn(Optional.empty());
    //when
    String email = "abc@abcd.com";
    String code = "abcd";
    CustomException customException = assertThrows(CustomException.class,
        () -> signUpService.verifyEmail(email, code));
    //then
    assertEquals(customException.getErrorCode(), ErrorCode.NOT_FOUND_EMAIL);
  }

  @Test
  @DisplayName("이메일 인증 시 인증코드 불일치할 경우 예외 처리 테스트")
  void verifyEmail_InvalidCode() {
    //given
    given(emailService.verifiedEmail(anyString(), anyString())).willReturn(false);
    //when
    String email = "abc@abcd.com";
    String code = "abcd";
    CustomException customException = assertThrows(CustomException.class,
        () -> signUpService.verifyEmail(email, code));
    //then
    assertEquals(customException.getErrorCode(), ErrorCode.INVALID_CODE);
  }
}