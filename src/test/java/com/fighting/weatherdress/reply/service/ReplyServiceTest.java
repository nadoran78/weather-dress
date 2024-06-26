package com.fighting.weatherdress.reply.service;

import static com.fighting.weatherdress.global.type.ErrorCode.MEMBER_IS_NOT_WRITER;
import static com.fighting.weatherdress.global.type.ErrorCode.MEMBER_NOT_FOUND;
import static com.fighting.weatherdress.global.type.ErrorCode.NOT_FOUND_REPLY;
import static com.fighting.weatherdress.global.type.ErrorCode.POST_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort.Direction;

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
    ReplyRegisterRequest request = ReplyRegisterRequest.builder()
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
    ReplyRegisterRequest request = ReplyRegisterRequest.builder()
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
    ReplyRegisterRequest request = ReplyRegisterRequest.builder()
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

  @Test
  void successGetReply() {
    //given
    Member member = Member.builder()
        .id(12L)
        .email("test@test.com")
        .nickName("귀여워")
        .build();
    Reply reply = spy(Reply.builder()
        .text("멋져요")
        .member(member)
        .post(Post.builder()
            .id(13L)
            .build())
        .build());
    LocalDateTime now = LocalDateTime.now();

    given(replyRepository.findById(anyLong())).willReturn(Optional.of(reply));
    given(reply.getId()).willReturn(12L);
    given(reply.getCreatedAt()).willReturn(now);
    //when
    ReplyResponse response = replyService.getReply(12L);
    //then
    assertEquals(12, response.getReplyId());
    assertEquals(reply.getText(), response.getText());
    assertEquals(member.getId(), response.getMember().getId());
    assertEquals(member.getEmail(), response.getMember().getEmail());
    assertEquals(member.getNickName(), response.getMember().getNickName());
    assertEquals(reply.getPost().getId(), response.getPostId());
    assertEquals(now, response.getCreatedAt());
  }

  @Test
  void getReply_shouldThrowNotFoundReply_whenReplyIsNotExist() {
    //given
    given(replyRepository.findById(anyLong())).willReturn(Optional.empty());

    //when
    CustomException customException = assertThrows(CustomException.class,
        () -> replyService.getReply(12L));
    //then
    assertEquals(NOT_FOUND_REPLY, customException.getErrorCode());
  }

  @Test
  void successGetReplyList() {
    //given
    Post post = Post.builder()
        .id(1L)
        .member(Member.builder()
            .nickName("닉네임")
            .build())
        .build();
    Reply reply1 = spy(Reply.builder()
        .text("멋져요1")
        .member(post.getMember())
        .build());
    Reply reply2 = spy(Reply.builder()
        .text("멋져요2")
        .member(post.getMember())
        .build());
    List<Reply> replies = List.of(reply1, reply2);
    LocalDateTime now = LocalDateTime.now();

    given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
    given(replyRepository.findAllByPost(any(Post.class), any(PageRequest.class)))
        .willReturn(replies);
    given(reply1.getId()).willReturn(1L);
    given(reply2.getId()).willReturn(2L);
    given(reply1.getCreatedAt()).willReturn(now);
    given(reply2.getCreatedAt()).willReturn(now);

    //when
    PageRequest pageRequest = PageRequest.of(0, 10, Direction.DESC, "createdAt");

    Slice<ReplyListDto> replyList = replyService.getReplyList(12L, pageRequest);
    //then
    List<ReplyListDto> content = replyList.getContent();

    assertEquals(1L, content.get(0).getReplyId());
    assertEquals(reply1.getText(), content.get(0).getText());
    assertEquals(reply1.getMember().getNickName(), content.get(0).getMemberNickname());
    assertEquals(now, content.get(0).getCreatedAt());
    assertEquals(2L, content.get(1).getReplyId());
    assertEquals(reply2.getText(), content.get(1).getText());
    assertEquals(reply2.getMember().getNickName(), content.get(1).getMemberNickname());
    assertEquals(now, content.get(1).getCreatedAt());
  }

  @Test
  void getReplyList_shouldThrowPostNotFound_whenPostIsNotExist() {
    //given
    given(postRepository.findById(anyLong())).willReturn(Optional.empty());

    //when
    PageRequest pageRequest = PageRequest.of(0, 10, Direction.DESC, "createdAt");

    CustomException customException = assertThrows(CustomException.class,
        () -> replyService.getReplyList(12L, pageRequest));
    //then
    assertEquals(POST_NOT_FOUND, customException.getErrorCode());
  }

  @Test
  void getReplyList_shouldThrowResponseListIsEmpty_whenReplyIsNotExist() {
    //given
    Post post = Post.builder()
        .id(1L)
        .member(Member.builder()
            .nickName("닉네임")
            .build())
        .build();
    List<Reply> replies = new ArrayList<>();

    given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
    given(replyRepository.findAllByPost(any(Post.class), any(PageRequest.class)))
        .willReturn(replies);

    //when
    PageRequest pageRequest = PageRequest.of(0, 10, Direction.DESC, "createdAt");

    Slice<ReplyListDto> replyList = replyService.getReplyList(12L, pageRequest);
    //then
    assertTrue(replyList.isEmpty());
  }

  @Test
  void successUpdateRequest() {
    //given
    Member member = Member.builder()
        .id(12L)
        .email("test@test.com")
        .nickName("별명")
        .build();
    Post post = Post.builder()
        .id(15L)
        .build();
    Reply reply = spy(Reply.builder()
        .text("멋있습니다.")
        .member(member)
        .post(post)
        .build());
    LocalDateTime now = LocalDateTime.now();
    ReplyUpdateRequest request = ReplyUpdateRequest.builder()
        .text("수정했습니다.")
        .build();

    given(replyRepository.findById(anyLong())).willReturn(Optional.of(reply));
    given(reply.getId()).willReturn(13L);
    given(reply.getCreatedAt()).willReturn(now);
    given(replyRepository.save(any(Reply.class))).will(returnsFirstArg());
    //when
    ReplyResponse replyResponse = replyService.updateReply(13L, request, 12L);
    //then
    assertEquals(request.getText(), replyResponse.getText());
    assertEquals(13L, replyResponse.getReplyId());
    assertEquals(member.getId(), replyResponse.getMember().getId());
    assertEquals(member.getEmail(), replyResponse.getMember().getEmail());
    assertEquals(member.getNickName(), replyResponse.getMember().getNickName());
    assertEquals(post.getId(), replyResponse.getPostId());
    assertEquals(now, replyResponse.getCreatedAt());
  }

  @Test
  void updateRequest_shouldThrowNotFoundReply_whenReplyIsNotExist() {
    //given
    ReplyUpdateRequest request = ReplyUpdateRequest.builder()
        .text("수정했습니다.")
        .build();

    given(replyRepository.findById(anyLong())).willReturn(Optional.empty());

    //when
    CustomException customException = assertThrows(CustomException.class,
        () -> replyService.updateReply(13L, request, 12L));

    //then
    assertEquals(NOT_FOUND_REPLY, customException.getErrorCode());
  }

  @Test
  void updateRequest_shouldThrowMemberIsNotWriter_whenMemberIsNotWriter() {
    //given
    Member member = Member.builder()
        .id(12L)
        .email("test@test.com")
        .nickName("별명")
        .build();
    Post post = Post.builder()
        .id(15L)
        .build();
    Reply reply = spy(Reply.builder()
        .text("멋있습니다.")
        .member(member)
        .post(post)
        .build());

    ReplyUpdateRequest request = ReplyUpdateRequest.builder()
        .text("수정했습니다.")
        .build();

    given(replyRepository.findById(anyLong())).willReturn(Optional.of(reply));

    //when
    CustomException customException = assertThrows(CustomException.class,
        () -> replyService.updateReply(13L, request, 16L));

    //then
    assertEquals(MEMBER_IS_NOT_WRITER, customException.getErrorCode());
  }

  @Test
  void successDeleteReply() {
    //given
    Reply reply = mock(Reply.class);
    Member member = Member.builder()
        .id(13L)
        .build();

    given(replyRepository.findById(anyLong())).willReturn(Optional.of(reply));
    given(reply.getMember()).willReturn(member);
    //when
    replyService.deleteReply(144L, 13L);
    //then
    ArgumentCaptor<Reply> argumentCaptor = ArgumentCaptor.forClass(Reply.class);

    verify(replyRepository).delete(argumentCaptor.capture());

    assertEquals(reply, argumentCaptor.getValue());
  }

  @Test
  void deleteReply_shouldThrowNotFoundReply_whenReplyIsNotExist() {
    //given
    given(replyRepository.findById(anyLong())).willReturn(Optional.empty());
    //when
    CustomException customException = assertThrows(CustomException.class,
        () -> replyService.deleteReply(144L, 13L));
    //then
    assertEquals(NOT_FOUND_REPLY, customException.getErrorCode());
  }

  @Test
  void deleteReply_shouldThrowMemberIsNotExist_whenMemberIsNotExist() {
    //given
    Reply reply = mock(Reply.class);
    Member member = Member.builder()
        .id(6L)
        .build();

    given(replyRepository.findById(anyLong())).willReturn(Optional.of(reply));
    given(reply.getMember()).willReturn(member);
    //when
    CustomException customException = assertThrows(CustomException.class,
        () -> replyService.deleteReply(144L, 13L));
    //then
    assertEquals(MEMBER_IS_NOT_WRITER, customException.getErrorCode());
  }
}