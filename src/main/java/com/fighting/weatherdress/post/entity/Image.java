package com.fighting.weatherdress.post.entity;

import com.fighting.weatherdress.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
@Entity
@AuditOverride(forClass = BaseEntity.class)
public class Image extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String url;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  private Post post;

  public static Image toEntity(String url, Post post) {
    Image image = Image.builder()
        .url(url)
        .post(post)
        .build();
    post.getImages().add(image);
    return image;
  }
}
