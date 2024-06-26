package com.fighting.weatherdress.reply.service;

import static com.fighting.weatherdress.global.type.ErrorCode.MEMBER_IS_NOT_WRITER;
import static com.fighting.weatherdress.global.type.ErrorCode.MEMBER_NOT_FOUND;
import static com.fighting.weatherdress.global.type.ErrorCode.NOT_FOUND_REPLY;
import static com.fighting.weatherdress.global.type.ErrorCode.POST_NOT_FOUND;

import com.fighting.weatherdress.global.exception.CustomException;
import com.fighting.weatherdress.member.domain.Member;
import com.fighting.weatherdress.member.repository.MemberRepository;
import com.fighting.weatherdress.post.entity.Post;
import com.fighting.weatherdress.post.repository.PostRepository;
import com.fighting.weatherdress.reply.dto.ReplyListDto;
import com.fighting.weatherdress.reply.dto.ReplyRegisterRequest;
import com.fighting.weatherdress.reply.dto.ReplyResponse;
import com.fighting.weatherdress.reply.dto.ReplyUpdateRequest;
import com.fighting.weatherdress.reply.entity.Reply;
import com.fighting.weatherdress.reply.repository.ReplyRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReplyService {

  private final ReplyRepository replyRepository;
  private final MemberRepository memberRepository;
  private final PostRepository postRepository;

  public void registerReply(ReplyRegisterRequest request, long userId) {
    Member member = memberRepository.findById(userId)
        .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

    Post post = postRepository.findById(request.getPostId())
        .orElseThrow(() -> new CustomException(POST_NOT_FOUND));

    Reply reply = Reply.builder()
        .text(request.getText())
        .member(member)
        .post(post)
        .build();
    replyRepository.save(reply);
  }

  public ReplyResponse getReply(long replyId) {
    Reply reply = replyRepository.findById(replyId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_REPLY));

    return ReplyResponse.fromEntity(reply);
  }

  public Slice<ReplyListDto> getReplyList(long postId, Pageable pageable) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(POST_NOT_FOUND));

    List<ReplyListDto> replyListDtos = replyRepository.findAllByPost(post, pageable)
        .stream()
        .map(ReplyListDto::fromEntity).toList();

    return new SliceImpl<>(replyListDtos);
  }

  public ReplyResponse updateReply(long replyId, ReplyUpdateRequest request,
      long userId) {
    Reply reply = findReplyAndCheckValidMember(replyId, userId);

    reply.updateText(request.getText());

    Reply savedReply = replyRepository.save(reply);

    return ReplyResponse.fromEntity(savedReply);
  }

  public void deleteReply(long replyId, long userId) {
    Reply reply = findReplyAndCheckValidMember(replyId, userId);

    replyRepository.delete(reply);
  }

  private Reply findReplyAndCheckValidMember(long replyId, long userId) {
    Reply reply = replyRepository.findById(replyId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_REPLY));

    if (reply.getMember().getId() != userId) {
      throw new CustomException(MEMBER_IS_NOT_WRITER);
    }

    return reply;
  }

}
