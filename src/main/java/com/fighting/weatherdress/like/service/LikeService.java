package com.fighting.weatherdress.like.service;

import static com.fighting.weatherdress.global.type.ErrorCode.ALREADY_REGISTERED_LIKE;
import static com.fighting.weatherdress.global.type.ErrorCode.INVALID_LIKE_REQUEST;
import static com.fighting.weatherdress.global.type.ErrorCode.MEMBER_NOT_FOUND;
import static com.fighting.weatherdress.global.type.ErrorCode.NOT_FOUND_REPLY;
import static com.fighting.weatherdress.global.type.ErrorCode.POST_NOT_FOUND;

import com.fighting.weatherdress.global.exception.CustomException;
import com.fighting.weatherdress.like.dto.LikeRegisterRequest;
import com.fighting.weatherdress.like.entity.Like;
import com.fighting.weatherdress.like.etc.LikeTarget;
import com.fighting.weatherdress.like.repository.LikeRepository;
import com.fighting.weatherdress.member.domain.Member;
import com.fighting.weatherdress.member.repository.MemberRepository;
import com.fighting.weatherdress.post.entity.Post;
import com.fighting.weatherdress.post.repository.PostRepository;
import com.fighting.weatherdress.reply.entity.Reply;
import com.fighting.weatherdress.reply.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

  private final LikeRepository likeRepository;
  private final PostRepository postRepository;
  private final ReplyRepository replyRepository;
  private final MemberRepository memberRepository;

  public void registerLike(LikeRegisterRequest request, long memberId) {
    boolean isLikeForPost = checkLikeForPost(request);

    LikeTarget likeTarget = getLikeTarget(request, isLikeForPost);

    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

    checkRegisteredOnlyOneLikeByMember(likeTarget, isLikeForPost, member);

    likeRepository.save(Like.toEntity(likeTarget, member, isLikeForPost));
  }

  private void checkRegisteredOnlyOneLikeByMember(LikeTarget likeTarget,
      boolean isLikeForPost, Member member) {
    if (isLikeForPost) {
      if (likeRepository.findByPostAndMember((Post) likeTarget, member).isPresent()) {
        throw new CustomException(ALREADY_REGISTERED_LIKE);
      }
    } else {
      if (likeRepository.findByReplyAndMember((Reply) likeTarget, member).isPresent()) {
        throw new CustomException(ALREADY_REGISTERED_LIKE);
      }
    }
  }

  private boolean checkLikeForPost(LikeRegisterRequest request) {
    if (request.getPostId() != null && request.getReplyId() == null) {
      return true;
    } else if (request.getReplyId() != null && request.getPostId() == null) {
      return false;
    } else {
      throw new CustomException(INVALID_LIKE_REQUEST);
    }
  }

  private LikeTarget getLikeTarget(LikeRegisterRequest request, boolean isLikeForPost) {
    LikeTarget likeTarget;
    if (isLikeForPost) {
      likeTarget = postRepository.findById(request.getPostId())
          .orElseThrow(() -> new CustomException(POST_NOT_FOUND));
    } else {
      likeTarget = replyRepository.findById(request.getReplyId())
          .orElseThrow(() -> new CustomException(NOT_FOUND_REPLY));
    }
    return likeTarget;
  }


}
