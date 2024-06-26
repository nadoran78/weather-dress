package com.fighting.weatherdress.post.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fighting.weatherdress.config.WithMockCustomUser;
import com.fighting.weatherdress.post.dto.PostRequest;
import com.fighting.weatherdress.post.dto.PostResponse;
import com.fighting.weatherdress.post.service.PostService;
import com.fighting.weatherdress.security.filter.JwtAuthenticationFilter;
import com.fighting.weatherdress.weather.dto.LocationDto;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;
import util.MockDataUtil;

@WebMvcTest(controllers = PostController.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)})
class PostControllerTest {

  @MockBean
  private PostService postService;
  @Autowired
  private MockMvc mockMvc;

  @Test
  @WithMockCustomUser
  void successRegisterPost() throws Exception {
    //given
    LocationDto locationDto = LocationDto.builder()
        .sido("경기도")
        .sigungu("군포시")
        .build();
    PostRequest request = PostRequest.builder()
        .content("오늘 이렇게 입었어요")
        .location(locationDto)
        .build();

    MockMultipartHttpServletRequestBuilder post = MockMvcRequestBuilders
        .multipart(POST, "/post")
        .part(MockDataUtil.createMockPart("request", request));

    for (int i = 1; i <= 3; i++) {
      MockMultipartFile mockMultipartFile = MockDataUtil.createMockMultipartFile("images",
          "src/test/resources/image/image" + i + ".png");
      post.file(mockMultipartFile);
    }

    //when
    ResultActions resultActions = mockMvc.perform(post.with(csrf()))
        .andDo(print());

    //then
    resultActions.andExpect(status().isOk());

    ArgumentCaptor<PostRequest> postRequestArgumentCaptor = ArgumentCaptor.forClass(
        PostRequest.class);
    ArgumentCaptor<List<MultipartFile>> listArgumentCaptor = ArgumentCaptor.forClass(
        List.class);
    ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);

    verify(postService).registerPost(postRequestArgumentCaptor.capture(),
        listArgumentCaptor.capture(), longArgumentCaptor.capture());

    assertEquals(request.getContent(), postRequestArgumentCaptor.getValue().getContent());
    assertEquals(request.getLocation().getSido(),
        postRequestArgumentCaptor.getValue().getLocation().getSido());
    assertEquals(request.getLocation().getSigungu(),
        postRequestArgumentCaptor.getValue().getLocation().getSigungu());
    assertEquals(3, listArgumentCaptor.getValue().size());
    assertEquals("image1.png",
        listArgumentCaptor.getValue().get(0).getOriginalFilename());
    assertEquals("image2.png",
        listArgumentCaptor.getValue().get(1).getOriginalFilename());
    assertEquals("image3.png",
        listArgumentCaptor.getValue().get(2).getOriginalFilename());
    assertEquals(1, longArgumentCaptor.getValue());

  }

  @Test
  @WithMockCustomUser
  void registerPost_shouldValidException_whenInvalidRequest()
      throws Exception {
    //given
    PostRequest request = PostRequest.builder()
        .content("1".repeat(301))
        .build();
    //when
    MockMultipartHttpServletRequestBuilder post = MockMvcRequestBuilders
        .multipart(POST, "/post")
        .part(MockDataUtil.createMockPart("request", request));

    for (int i = 1; i <= 3; i++) {
      MockMultipartFile mockMultipartFile = MockDataUtil.createMockMultipartFile("images",
          "src/test/resources/image/image" + i + ".png");
      post.file(mockMultipartFile);
    }

    //when & then
    mockMvc.perform(post.with(csrf()))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.location").exists())
        .andExpect(jsonPath("$.content").exists());

  }

  @Test
  @WithMockCustomUser
  void success_updatePost() throws Exception {
    //given
    LocationDto locationDto = LocationDto.builder()
        .sido("경기도")
        .sigungu("군포시")
        .build();
    PostRequest request = PostRequest.builder()
        .content("오늘 이렇게 입었어요")
        .location(locationDto)
        .build();

    MockMultipartHttpServletRequestBuilder post = MockMvcRequestBuilders
        .multipart(PUT, "/post/14")
        .part(MockDataUtil.createMockPart("request", request));

    for (int i = 1; i <= 3; i++) {
      MockMultipartFile mockMultipartFile = MockDataUtil.createMockMultipartFile("images",
          "src/test/resources/image/image" + i + ".png");
      post.file(mockMultipartFile);
    }

    //when
    ResultActions resultActions = mockMvc.perform(post.with(csrf()))
        .andDo(print());

    //then
    resultActions.andExpect(status().isOk());

    ArgumentCaptor<PostRequest> postRequestArgumentCaptor = ArgumentCaptor.forClass(
        PostRequest.class);
    ArgumentCaptor<List<MultipartFile>> listArgumentCaptor = ArgumentCaptor.forClass(
        List.class);
    ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);

    verify(postService).updatePost(postRequestArgumentCaptor.capture(),
        listArgumentCaptor.capture(), longArgumentCaptor.capture(),
        longArgumentCaptor.capture());

    assertEquals(request.getContent(), postRequestArgumentCaptor.getValue().getContent());
    assertEquals(request.getLocation().getSido(),
        postRequestArgumentCaptor.getValue().getLocation().getSido());
    assertEquals(request.getLocation().getSigungu(),
        postRequestArgumentCaptor.getValue().getLocation().getSigungu());
    assertEquals(3, listArgumentCaptor.getValue().size());
    assertEquals("image1.png",
        listArgumentCaptor.getValue().get(0).getOriginalFilename());
    assertEquals("image2.png",
        listArgumentCaptor.getValue().get(1).getOriginalFilename());
    assertEquals("image3.png",
        listArgumentCaptor.getValue().get(2).getOriginalFilename());
    assertEquals(14, longArgumentCaptor.getAllValues().get(0));
    assertEquals(1, longArgumentCaptor.getAllValues().get(1));
  }

  @Test
  @WithMockCustomUser
  void updatePost_shouldValidException_whenInvalidRequest()
      throws Exception {
    //given
    PostRequest request = PostRequest.builder()
        .content("1".repeat(301))
        .build();
    //when
    MockMultipartHttpServletRequestBuilder post = MockMvcRequestBuilders
        .multipart(PUT, "/post/14")
        .part(MockDataUtil.createMockPart("request", request));

    for (int i = 1; i <= 3; i++) {
      MockMultipartFile mockMultipartFile = MockDataUtil.createMockMultipartFile("images",
          "src/test/resources/image/image" + i + ".png");
      post.file(mockMultipartFile);
    }

    //when & then
    mockMvc.perform(post.with(csrf()))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.location").exists())
        .andExpect(jsonPath("$.content").exists());

  }

  @Test
  @WithMockUser
  void successGetPost() throws Exception {
    //given
    PostResponse response = PostResponse.builder()
        .id(1L)
        .text("이렇게 입었어요.")
        .build();
    given(postService.getPost(anyLong())).willReturn(response);
    //when & then
    mockMvc.perform(get("/post/1")
            .contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value("1"))
        .andExpect(jsonPath("$.text").value(response.getText()));
  }

  @Test
  @WithMockUser
  void successGetPostList() throws Exception {
    //given
    PostResponse response = PostResponse.builder()
        .id(1L)
        .text("이렇게 입었어요.")
        .build();
    Slice<PostResponse> responseSlice = new SliceImpl<>(List.of(response));
    given(postService.getPostList(any(Pageable.class))).willReturn(responseSlice);
    //when & then
    mockMvc.perform(get("/post/list")
            .contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(jsonPath("$.content[0].id").value("1"))
        .andExpect(jsonPath("$.content[0].text").value(response.getText()));
  }

  @Test
  @WithMockCustomUser
  void successDeletePost() throws Exception {
    //when
    mockMvc.perform(delete("/post/4")
            .contentType(APPLICATION_JSON)
            .with(csrf()))
        .andExpect(status().isOk());

    ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
    verify(postService).deletePost(longArgumentCaptor.capture(),
        longArgumentCaptor.capture());
    //then
    assertEquals(4, longArgumentCaptor.getAllValues().get(0));
    assertEquals(1, longArgumentCaptor.getAllValues().get(1));
  }
}