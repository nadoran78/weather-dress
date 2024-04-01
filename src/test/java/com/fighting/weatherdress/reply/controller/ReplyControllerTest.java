package com.fighting.weatherdress.reply.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fighting.weatherdress.config.WithMockCustomUser;
import com.fighting.weatherdress.reply.dto.ReplyRequest;
import com.fighting.weatherdress.reply.service.ReplyService;
import com.fighting.weatherdress.security.filter.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
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
    ReplyRequest request = ReplyRequest.builder()
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
    ArgumentCaptor<ReplyRequest> requestArgumentCaptor = ArgumentCaptor.forClass(
        ReplyRequest.class);
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
    ReplyRequest request = ReplyRequest.builder()
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

}