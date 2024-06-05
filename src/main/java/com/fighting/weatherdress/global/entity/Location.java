package com.fighting.weatherdress.global.entity;

import com.fighting.weatherdress.post.entity.Post;
import com.fighting.weatherdress.weather.entity.WeeklyWeather;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
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
  private String locationCode;  // 미시 예보구역코드(시군구 레벨의 예보지역코드) : 중기 온도예보 조회 시 필요

  @Column(nullable = false)
  private String locationLandCode;  // 거시 예보구역코드(시도 레벨의 예보지역코드) : 중기 날씨예보 조회 시 필요

  @Builder.Default
  @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Post> posts = new ArrayList<>();

}
