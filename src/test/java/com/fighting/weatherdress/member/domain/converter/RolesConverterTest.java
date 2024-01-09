package com.fighting.weatherdress.member.domain.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fighting.weatherdress.member.domain.Member;
import com.fighting.weatherdress.member.dto.SignUpDto;
import com.fighting.weatherdress.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RolesConverterTest {

  @Autowired
  private MemberRepository memberRepository;

  @Test
  void successConverter() {
    //given
    SignUpDto request = SignUpDto.builder()
        .email("abc@abcd.com")
        .password("1234")
        .nickName("아리아리")
        .build();
    Member member = Member.fromDto(request);
    //when
    Member save = memberRepository.save(member);
    //then
    assertEquals(save, member);
    memberRepository.delete(save);
  }


}