package com.fighting.weatherdress.reply.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fighting.weatherdress.config.WithMockCustomUser;
import com.fighting.weatherdress.reply.dto.ReplyListDto;
import com.fighting.weatherdress.reply.dto.ReplyRegisterRequest;
import com.fighting.weatherdress.reply.dto.ReplyResponse;
import com.fighting.weatherdress.reply.dto.ReplyUpdateRequest;
import com.fighting.weatherdress.reply.service.ReplyService;
import com.fighting.weatherdress.security.dto.MemberInfoDto;
import com.fighting.weatherdress.security.filter.JwtAuthenticationFilter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ReplyController.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)})
class ReplyControllerTest {

  @MockBean
  private ReplyService replyService;
  @Autowired
  private MockMvc mockMvc;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @WithMockCustomUser
  void successRegisterReply() throws Exception {
    //given
    ReplyRegisterRequest request = ReplyRegisterRequest.builder()
        .postId(1L)
        .text("잘 봤습니다.")
        .build();
    //when
    mockMvc.perform(post("/reply")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()))
        .andDo(print())
        .andExpect(status().isOk());
    //then
    ArgumentCaptor<ReplyRegisterRequest> requestArgumentCaptor = ArgumentCaptor.forClass(
        ReplyRegisterRequest.class);
    ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);

    verify(replyService).registerReply(requestArgumentCaptor.capture(),
        longArgumentCaptor.capture());

    assertEquals(request.getPostId(), requestArgumentCaptor.getValue().getPostId());
    assertEquals(request.getText(), requestArgumentCaptor.getValue().getText());
    assertEquals(1, longArgumentCaptor.getValue());
  }

  @Test
  @WithMockCustomUser
  void registerReply_shouldThrowInvalidRequest_whenTextSizeIsOver100() throws Exception {
    //given
    ReplyRegisterRequest request = ReplyRegisterRequest.builder()
        .postId(1L)
        .text("1".repeat(101))
        .build();
    //when & then
    mockMvc.perform(post("/reply")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.text").value("size must be between 0 and 100"));

  }

  @Test
  @WithMockUser
  void successGetReply() throws Exception {
    //given
    LocalDateTime now = LocalDateTime.now();
    ReplyResponse response = ReplyResponse.builder()
        .replyId(12L)
        .member(MemberInfoDto.builder()
            .id(1L)
            .email("test@test.com")
            .nickName("귀여워")
            .build())
        .text("멋져요")
        .postId(13L)
        .createdAt(now)
        .build();

    given(replyService.getReply(anyLong())).willReturn(response);
    //when & then
    mockMvc.perform(get("/reply/12")
            .with(csrf()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.replyId").value(response.getReplyId()))
        .andExpect(jsonPath("$.text").value(response.getText()))
        .andExpect(jsonPath("$.member.id").value(response.getMember().getId()))
        .andExpect(jsonPath("$.member.email").value(response.getMember().getEmail()))
        .andExpect(
            jsonPath("$.member.nickName").value(response.getMember().getNickName()))
        .andExpect(jsonPath("$.postId").value(response.getPostId()))
        .andExpect(jsonPath("$.createdAt").value(now.toString()));
  }

  @Test
  @WithMockUser
  void successGetReplyList() throws Exception {
    //given
    List<ReplyListDto> replyListDtos = new ArrayList<>();
    LocalDateTime now = LocalDateTime.now();
    for (int i = 1; i <= 3; i++) {
      replyListDtos.add(ReplyListDto.builder()
          .replyId(i)
          .text("멋져요" + i)
          .memberNickname("닉네임" + i)
          .createdAt(now.minusDays(i))
          .build());
    }

    given(replyService.getReplyList(anyLong(), any(PageRequest.class))).willReturn(
        new SliceImpl<>(replyListDtos));
    //when & then
    mockMvc.perform(get("/reply/list/1")
            .with(csrf()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(
            jsonPath("$.content[0].replyId").value(replyListDtos.get(0).getReplyId()))
        .andExpect(jsonPath("$.content[0].text").value(replyListDtos.get(0).getText()))
        .andExpect(jsonPath("$.content[0].memberNickname").value(
            replyListDtos.get(0).getMemberNickname()))
        .andExpect(
            jsonPath("$.content[0].createdAt").value(
                replyListDtos.get(0).getCreatedAt().toString()))
        .andExpect(
            jsonPath("$.content[1].replyId").value(replyListDtos.get(1).getReplyId()))
        .andExpect(jsonPath("$.content[1].text").value(replyListDtos.get(1).getText()))
        .andExpect(jsonPath("$.content[1].memberNickname").value(
            replyListDtos.get(1).getMemberNickname()))
        .andExpect(
            jsonPath("$.content[1].createdAt").value(
                replyListDtos.get(1).getCreatedAt().toString()))
        .andExpect(
            jsonPath("$.content[2].replyId").value(replyListDtos.get(2).getReplyId()))
        .andExpect(jsonPath("$.content[2].text").value(replyListDtos.get(2).getText()))
        .andExpect(jsonPath("$.content[2].memberNickname").value(
            replyListDtos.get(2).getMemberNickname()))
        .andExpect(
            jsonPath("$.content[2].createdAt").value(
                replyListDtos.get(2).getCreatedAt().toString()));
  }

  @Test
  @WithMockCustomUser
  void successUpdateReply() throws Exception {
    //given
    LocalDateTime now = LocalDateTime.now();
    ReplyResponse response = ReplyResponse.builder()
        .replyId(12L)
        .text("멋져요")
        .member(MemberInfoDto.builder()
            .id(13L)
            .email("test@test.com")
            .nickName("벌거숭이")
            .build())
        .postId(14L)
        .createdAt(now)
        .build();
    ReplyUpdateRequest request = ReplyUpdateRequest.builder()
        .text("멋져요 수정")
        .build();

    given(replyService.updateReply(anyLong(), any(ReplyUpdateRequest.class), anyLong()))
        .willReturn(response);
    //when & then
    mockMvc.perform(patch("/reply/12")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()))
        .andDo(print())
        .andExpect(jsonPath("$.replyId").value(response.getReplyId()))
        .andExpect(jsonPath("$.text").value(response.getText()))
        .andExpect(jsonPath("$.member.id").value(response.getMember().getId()))
        .andExpect(jsonPath("$.member.email").value(response.getMember().getEmail()))
        .andExpect(
            jsonPath("$.member.nickName").value(response.getMember().getNickName()))
        .andExpect(jsonPath("$.postId").value(response.getPostId()))
        .andExpect(jsonPath("$.createdAt").value(response.getCreatedAt().toString()));
  }

  @Test
  @WithMockCustomUser
  void updateReply_shouldThrowInvalidException_whenTextSizeIsOver100() throws Exception {
    //given
    LocalDateTime now = LocalDateTime.now();
    ReplyResponse response = ReplyResponse.builder()
        .replyId(12L)
        .text("멋져요")
        .member(MemberInfoDto.builder()
            .id(13L)
            .email("test@test.com")
            .nickName("벌거숭이")
            .build())
        .postId(14L)
        .createdAt(now)
        .build();
    ReplyUpdateRequest request = ReplyUpdateRequest.builder()
        .text("1".repeat(101))
        .build();

    given(replyService.updateReply(anyLong(), any(ReplyUpdateRequest.class), anyLong()))
        .willReturn(response);
    //when & then
    mockMvc.perform(patch("/reply/12")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.text").exists());
  }
}