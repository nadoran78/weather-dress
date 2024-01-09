package com.fighting.weatherdress.member.domain;

import com.fighting.weatherdress.global.entity.BaseEntity;
import com.fighting.weatherdress.global.type.Authority;
import com.fighting.weatherdress.member.domain.converter.RolesConverter;
import com.fighting.weatherdress.member.dto.SignUpDto;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@AuditOverride(forClass = BaseEntity.class)
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String email;

  @Column
  private String password;

  @Column(nullable = false)
  private String nickName;

  @Column(nullable = false)
  private boolean verified;

  @Convert(converter = RolesConverter.class)
  private List<String> roles;

  public static Member fromDto(SignUpDto request) {
    return Member.builder()
        .email(request.getEmail())
        .password(request.getPassword())
        .nickName(request.getNickName())
        .verified(false)
        .roles(Collections.singletonList(Authority.ROLE_USER.toString()))
        .build();
  }
}
