package com.fighting.weatherdress.reply.service;

import static com.fighting.weatherdress.global.type.ErrorCode.MEMBER_NOT_FOUND;
import static com.fighting.weatherdress.global.type.ErrorCode.POST_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.fighting.weatherdress.global.exception.CustomException;
import com.fighting.weatherdress.member.domain.Member;
import com.fighting.weatherdress.member.repository.MemberRepository;
import com.fighting.weatherdress.post.entity.Post;
import com.fighting.weatherdress.post.repository.PostRepository;
import com.fighting.weatherdress.reply.dto.ReplyRequest;
import com.fighting.weatherdress.reply.entity.Reply;
import com.fighting.weatherdress.reply.repository.ReplyRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReplyServiceTest {

  @Mock
  private ReplyRepository replyRepository;
  @Mock
  private PostRepository postRepository;
  @Mock
  private MemberRepository memberRepository;
  @InjectMocks
  private ReplyService replyService;

  @Test
  void successRegisterReply() {
    //given
    ReplyRequest request = ReplyRequest.builder()
        .postId(14L)
        .text("멋져요.")
        .build();
    Member member = Member.builder()
        .id(1L)
        .email("test@email.com")
        .build();
    Post post = Post.builder()
        .id(14L)
        .text("오늘 이렇게 입었어요.")
        .build();

    given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
    given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
    //when
    replyService.registerReply(request, 1L);
    //then
    ArgumentCaptor<Reply> argumentCaptor = ArgumentCaptor.forClass(Reply.class);

    verify(replyRepository).save(argumentCaptor.capture());

    assertEquals(request.getText(), argumentCaptor.getValue().getText());
    assertEquals(post, argumentCaptor.getValue().getPost());
    assertEquals(member, argumentCaptor.getValue().getMember());
  }

  @Test
  void registerReply_shouldThrowMemberNotFound_whenMemberIsNotExist() {
    //given
    ReplyRequest request = ReplyRequest.builder()
        .postId(14L)
        .text("멋져요.")
        .build();

    given(memberRepository.findById(anyLong())).willReturn(Optional.empty());
    //when
    CustomException customException = assertThrows(CustomException.class,
        () -> replyService.registerReply(request, 1L));
    //then
    assertEquals(MEMBER_NOT_FOUND, customException.getErrorCode());
  }

  @Test
  void registerReply_shouldThrowPostNotFound_whenPostIsNotExist() {
    //given
    ReplyRequest request = ReplyRequest.builder()
        .postId(14L)
        .text("멋져요.")
        .build();
    Member member = Member.builder()
        .id(1L)
        .email("test@email.com")
        .build();

    given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
    given(postRepository.findById(anyLong())).willReturn(Optional.empty());
    //when
    CustomException customException = assertThrows(CustomException.class,
        () -> replyService.registerReply(request, 1L));
    //then
    assertEquals(POST_NOT_FOUND, customException.getErrorCode());
  }


}