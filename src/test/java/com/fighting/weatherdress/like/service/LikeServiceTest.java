package com.fighting.weatherdress.like.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.fighting.weatherdress.global.exception.CustomException;
import com.fighting.weatherdress.global.type.ErrorCode;
import com.fighting.weatherdress.like.dto.LikeRegisterRequest;
import com.fighting.weatherdress.like.entity.Like;
import com.fighting.weatherdress.like.repository.LikeRepository;
import com.fighting.weatherdress.member.domain.Member;
import com.fighting.weatherdress.member.repository.MemberRepository;
import com.fighting.weatherdress.post.entity.Post;
import com.fighting.weatherdress.post.repository.PostRepository;
import com.fighting.weatherdress.reply.entity.Reply;
import com.fighting.weatherdress.reply.repository.ReplyRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

  @Mock
  private LikeRepository likeRepository;
  @Mock
  private PostRepository postRepository;
  @Mock
  private ReplyRepository replyRepository;
  @Mock
  private MemberRepository memberRepository;
  @InjectMocks
  private LikeService likeService;

  @Test
  void successRegisterLikeForPost() {
    //given
    LikeRegisterRequest request = LikeRegisterRequest.builder()
        .postId(13L)
        .build();
    Post post = mock(Post.class);
    Member member = mock(Member.class);
    given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
    given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
    //when
    likeService.registerLike(request, 133L);
    //then
    ArgumentCaptor<Like> argumentCaptor = ArgumentCaptor.forClass(Like.class);

    verify(likeRepository).save(argumentCaptor.capture());

    assertEquals(member, argumentCaptor.getValue().getMember());
    assertEquals(post, argumentCaptor.getValue().getPost());
  }

  @Test
  void successRegisterLikeForReply() {
    //given
    LikeRegisterRequest request = LikeRegisterRequest.builder()
        .replyId(13L)
        .build();
    Reply reply = mock(Reply.class);
    Member member = mock(Member.class);

    given(replyRepository.findById(anyLong())).willReturn(Optional.of(reply));
    given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
    //when
    likeService.registerLike(request, 133L);
    //then
    ArgumentCaptor<Like> argumentCaptor = ArgumentCaptor.forClass(Like.class);

    verify(likeRepository).save(argumentCaptor.capture());

    assertEquals(member, argumentCaptor.getValue().getMember());
    assertEquals(reply, argumentCaptor.getValue().getReply());
  }

  @Test
  void registerLike_throwInvalidLikeRequest_whenBothPostAndReplyIsNull() {
    //given
    LikeRegisterRequest request = LikeRegisterRequest.builder().build();
    //when
    CustomException customException = assertThrows(CustomException.class,
        () -> likeService.registerLike(request, 133L));
    //then
    assertEquals(customException.getErrorCode(), ErrorCode.INVALID_LIKE_REQUEST);
  }

  @Test
  void registerLike_throwInvalidLikeRequest_whenBothPostAndReplyIsNotNull() {
    //given
    LikeRegisterRequest request = LikeRegisterRequest.builder()
        .postId(123L)
        .replyId(1234L)
        .build();
    //when
    CustomException customException = assertThrows(CustomException.class,
        () -> likeService.registerLike(request, 133L));
    //then
    assertEquals(customException.getErrorCode(), ErrorCode.INVALID_LIKE_REQUEST);
  }

  @Test
  void registerLike_throwPostNotFound_whenPostIdIsNotExist() {
    //given
    LikeRegisterRequest request = LikeRegisterRequest.builder()
        .postId(13L)
        .build();

    given(postRepository.findById(anyLong())).willReturn(Optional.empty());
    //when
    CustomException customException = assertThrows(CustomException.class,
        () -> likeService.registerLike(request, 133L));
    //then
    assertEquals(customException.getErrorCode(), ErrorCode.POST_NOT_FOUND);
  }

  @Test
  void registerLike_throwNotFoundReply_whenReplyIdIsNotExist() {
    //given
    LikeRegisterRequest request = LikeRegisterRequest.builder()
        .replyId(13L)
        .build();

    given(replyRepository.findById(anyLong())).willReturn(Optional.empty());
    //when
    CustomException customException = assertThrows(CustomException.class,
        () -> likeService.registerLike(request, 133L));
    //then
    assertEquals(customException.getErrorCode(), ErrorCode.NOT_FOUND_REPLY);
  }

  @Test
  void registerLike_throwMemberNotFound_whenMemberIdIsNotExist() {
    //given
    LikeRegisterRequest request = LikeRegisterRequest.builder()
        .postId(13L)
        .build();
    Post post = mock(Post.class);

    given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
    given(memberRepository.findById(anyLong())).willReturn(Optional.empty());
    //when
    CustomException customException = assertThrows(CustomException.class,
        () -> likeService.registerLike(request, 133L));
    //then
    assertEquals(customException.getErrorCode(), ErrorCode.MEMBER_NOT_FOUND);
  }

}