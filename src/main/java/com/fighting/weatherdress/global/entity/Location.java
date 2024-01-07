package com.fighting.weatherdress.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@AuditOverride(forClass = BaseEntity.class)
public class Location extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String sido;

  @Column(nullable = false)
  private String sigungu;

  @Column(nullable = false)
  private int xCoordinate; // 기상청 x 좌표

  @Column(nullable = false)
  private int yCoordinate; // 기상청 y 좌표

  @Column(nullable = false)
  private String locationCode;

}
