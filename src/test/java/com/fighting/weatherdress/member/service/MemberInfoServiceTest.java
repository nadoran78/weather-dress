package com.fighting.weatherdress.member.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fighting.weatherdress.global.exception.CustomException;
import com.fighting.weatherdress.global.type.ErrorCode;
import com.fighting.weatherdress.mail.service.EmailService;
import com.fighting.weatherdress.member.domain.Member;
import com.fighting.weatherdress.member.dto.ChangePasswordDto;
import com.fighting.weatherdress.member.repository.MemberRepository;
import com.fighting.weatherdress.security.dto.MemberInfoDto;
import com.fighting.weatherdress.security.token.TokenProvider;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class MemberInfoServiceTest {

  @Mock
  private MemberRepository memberRepository;
  @Mock
  private EmailService emailService;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private TokenProvider tokenProvider;
  @InjectMocks
  private MemberInfoService memberInfoService;

  @Test
  @DisplayName("비밀번호 변경 성공 테스트")
  void successChangePassword() {
    //given
    Member member = Member.builder().build();

    given(emailService.verifiedEmail(anyString(), anyString())).willReturn(true);
    given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));
    given(passwordEncoder.encode(anyString())).willReturn("newPassword");
    //when
    ChangePasswordDto request = ChangePasswordDto.builder()
        .newPassword("abc")
        .code("code")
        .build();
    memberInfoService.changePassword("email", request);
    //then
    ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);
    verify(memberRepository, times(1)).save(captor.capture());
    assertEquals(captor.getValue().getPassword(), "newPassword");
  }

  @Test
  @DisplayName("비밀번호 변경 요청 시 db에 저장된 회원 이메일이 아닐 경우 예외 처리")
  void changePassword_NotFoundEmail() {
    //given
    given(emailService.verifiedEmail(anyString(), anyString())).willReturn(true);
    given(memberRepository.findByEmail(anyString())).willReturn(Optional.empty());
    //when
    ChangePasswordDto request = ChangePasswordDto.builder()
        .newPassword("abc")
        .code("code")
        .build();
    CustomException customException = assertThrows(CustomException.class,
        () -> memberInfoService.changePassword("email", request));
    //then
    assertEquals(customException.getErrorCode(), ErrorCode.NOT_FOUND_EMAIL);
  }

  @Test
  @DisplayName("비밀번호 변경 요청 시 이메일 인증 통과 못 할 경우 예외처리")
  void changePassword_InvalidCode() {
    //given
    given(emailService.verifiedEmail(anyString(), anyString())).willReturn(false);
    //when
    ChangePasswordDto request = ChangePasswordDto.builder()
        .newPassword("abc")
        .code("code")
        .build();
    CustomException customException = assertThrows(CustomException.class,
        () -> memberInfoService.changePassword("email", request));
    //then
    assertEquals(customException.getErrorCode(), ErrorCode.INVALID_CODE);
  }

  @Test
  @DisplayName("회원정보 조회 성공 테스트")
  void successGetMemberInfo() {
    //given
    Member member = Member.builder()
        .email("resultEmail")
        .nickName("nickName")
        .build();

    given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));
    //when
    MemberInfoDto memberInfoDto = memberInfoService.getMemberInfo("email");
    //then
    assertEquals(memberInfoDto.getEmail(), "resultEmail");
    assertEquals(memberInfoDto.getNickName(), "nickName");
  }

  @Test
  @DisplayName("회원정보 조회 시 가입된 회원이 아닐 경우 예외처리")
  void getMemberInfo_NotFoundEmail() {
    //given
    given(memberRepository.findByEmail(anyString())).willReturn(Optional.empty());
    //when
    CustomException customException = assertThrows(CustomException.class,
        () -> memberInfoService.getMemberInfo("email"));
    //then
    assertEquals(customException.getErrorCode(), ErrorCode.NOT_FOUND_EMAIL);
  }

  @Test
  @DisplayName("회원탈퇴 성공 테스트")
  void successDeleteMember() {
    //given
    Member member = Member.builder()
        .email("resultEmail")
        .nickName("nickName")
        .build();

    given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));
    //when
    memberInfoService.deleteMember("email");
    //then
    ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);
    verify(memberRepository, times(1)).delete(captor.capture());
    assertEquals(captor.getValue().getEmail(), "resultEmail");
    assertEquals(captor.getValue().getNickName(), "nickName");
  }
}