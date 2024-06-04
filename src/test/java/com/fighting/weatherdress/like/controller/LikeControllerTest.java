package com.fighting.weatherdress.like.controller;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fighting.weatherdress.config.WithMockCustomUser;
import com.fighting.weatherdress.like.dto.LikeRequest;
import com.fighting.weatherdress.like.service.LikeService;
import com.fighting.weatherdress.security.filter.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = LikeController.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)})
class LikeControllerTest {

  @MockBean
  private LikeService likeService;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @WithMockCustomUser
  void successRegisterLike() throws Exception {
    //given
    LikeRequest request = LikeRequest.builder()
        .postId(1L)
        .build();
    //when
    mockMvc.perform(post("/like")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .with(csrf()))
        .andDo(print())
        .andExpect(status().isOk());
    //then
    verify(likeService).registerLike(request, 1L);
  }

  @Test
  @WithMockCustomUser
  void successCancelLike() throws Exception {
    //given
    LikeRequest request = LikeRequest.builder()
        .postId(1L)
        .build();
    //when
    mockMvc.perform(delete("/like")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .with(csrf()))
        .andDo(print())
        .andExpect(status().isOk());
    //then
    verify(likeService).cancelLike(request, 1L);
  }
}