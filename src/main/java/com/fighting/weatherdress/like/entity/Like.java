package com.fighting.weatherdress.like.entity;

import com.fighting.weatherdress.global.entity.BaseEntity;
import com.fighting.weatherdress.like.dto.LikeRegisterRequest;
import com.fighting.weatherdress.like.etc.LikeTarget;
import com.fighting.weatherdress.member.domain.Member;
import com.fighting.weatherdress.post.entity.Post;
import com.fighting.weatherdress.reply.entity.Reply;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Like extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  @NotNull
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  private Post post;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reply_id")
  private Reply reply;

  public static Like toEntity(LikeTarget likeTarget, Member member, boolean isLikeForPost) {
    Like like;
    if (isLikeForPost) {
      like = Like.builder()
          .member(member)
          .post((Post) likeTarget)
          .build();
    } else {
      like = Like.builder()
          .member(member)
          .reply((Reply) likeTarget)
          .build();
    }
    return like;
  }

}
