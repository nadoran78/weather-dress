package com.fighting.weatherdress.member.domain;

import com.fighting.weatherdress.global.BaseEntity;
import com.fighting.weatherdress.global.type.Authority;
import com.fighting.weatherdress.member.dto.SignUpDto;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String nickName;

  @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
  private List<String> roles;

  public static Member fromDto(SignUpDto request) {
    return Member.builder()
        .email(request.getEmail())
        .password(request.getPassword())
        .nickName(request.getNickName())
        .roles(Collections.singletonList(Authority.ROLE_USER.toString()))
        .build();
  }
}
