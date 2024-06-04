package com.fighting.weatherdress.like.repository;

import com.fighting.weatherdress.like.entity.Like;
import com.fighting.weatherdress.like.etc.LikeTarget;
import com.fighting.weatherdress.member.domain.Member;
import com.fighting.weatherdress.post.entity.Post;
import com.fighting.weatherdress.reply.entity.Reply;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
  Optional<Like> findByPostAndMember(Post post, Member member);
  Optional<Like> findByReplyAndMember(Reply reply, Member member);
  Optional<Like> findByReply_IdAndMember_Id(long replyId, long memberId);
  Optional<Like> findByPost_IdAndMember_Id(long postId, long memberId);
}
