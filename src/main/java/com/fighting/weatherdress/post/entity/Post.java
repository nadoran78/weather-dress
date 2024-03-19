package com.fighting.weatherdress.post.entity;

import com.fighting.weatherdress.global.entity.BaseEntity;
import com.fighting.weatherdress.global.entity.Location;
import com.fighting.weatherdress.member.domain.Member;
import com.fighting.weatherdress.post.dto.PostRequest;
import com.fighting.weatherdress.weather.dto.ShortTermWeatherResponse;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@AuditOverride(forClass = BaseEntity.class)
public class Post extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String text;

  @Column(nullable = false)
  private int minTemperature;

  @Column(nullable = false)
  private int maxTemperature;

  @Column(nullable = false)
  private Long likeCount;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "location_id")
  private Location location;

  @Builder.Default
  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Image> images = new ArrayList<>();

  public static Post toEntity(String text, ShortTermWeatherResponse weather,
      Member member,
      Location location) {
    return Post.builder()
        .text(text)
        .minTemperature(weather.getToday().getMinTemperature())
        .maxTemperature(weather.getToday().getMaxTemperature())
        .likeCount(0L)
        .member(member)
        .location(location)
        .build();
  }

  public void updatePost(String content, ShortTermWeatherResponse weather,
      Location location) {
    this.text = content;
    this.minTemperature = weather.getToday().getMinTemperature();
    this.maxTemperature = weather.getToday().getMaxTemperature();
    this.location = location;
  }
}
