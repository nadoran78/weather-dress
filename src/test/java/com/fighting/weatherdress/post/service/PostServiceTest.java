package com.fighting.weatherdress.post.service;

import static com.fighting.weatherdress.global.type.ErrorCode.MEMBER_IS_NOT_WRITER;
import static com.fighting.weatherdress.global.type.ErrorCode.MEMBER_NOT_FOUND;
import static com.fighting.weatherdress.global.type.ErrorCode.NOT_FOUND_LOCATION;
import static com.fighting.weatherdress.global.type.ErrorCode.POST_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fighting.weatherdress.global.entity.Location;
import com.fighting.weatherdress.global.exception.CustomException;
import com.fighting.weatherdress.global.repository.LocationRepository;
import com.fighting.weatherdress.member.domain.Member;
import com.fighting.weatherdress.member.repository.MemberRepository;
import com.fighting.weatherdress.post.dto.PostRequest;
import com.fighting.weatherdress.post.entity.Image;
import com.fighting.weatherdress.post.entity.Post;
import com.fighting.weatherdress.post.repository.ImageRepository;
import com.fighting.weatherdress.post.repository.PostRepository;
import com.fighting.weatherdress.s3.dto.S3FileDto;
import com.fighting.weatherdress.s3.service.S3FileService;
import com.fighting.weatherdress.weather.dto.DailyShortWeather;
import com.fighting.weatherdress.weather.dto.LocationDto;
import com.fighting.weatherdress.weather.dto.ShortTermWeatherResponse;
import com.fighting.weatherdress.weather.service.ShortTermWeatherService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import util.MockDataUtil;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

  @Mock
  private ShortTermWeatherService shortTermWeatherService;
  @Mock
  private PostRepository postRepository;
  @Mock
  private MemberRepository memberRepository;
  @Mock
  private S3FileService fileService;
  @Mock
  private LocationRepository locationRepository;
  @Mock
  private ImageRepository imageRepository;
  @InjectMocks
  private PostService postService;

  private final PostRequest request = PostRequest.builder()
      .content("이렇게 입었어요.")
      .location(LocationDto.builder()
          .sido("경기도")
          .sigungu("군포시")
          .build())
      .build();

  @Test
  void successRegisterPost() throws URISyntaxException, IOException {
    //given
    Member member = Member.builder()
        .id(1L)
        .nickName("꼬북이")
        .build();
    Location location = Location.builder()
        .sido("경기도")
        .sigungu("군포시")
        .build();
    DailyShortWeather dailyShortWeather = DailyShortWeather.builder()
        .date("20240313")
        .maxTemperature(12)
        .minTemperature(9)
        .build();
    ShortTermWeatherResponse response = ShortTermWeatherResponse.builder()
        .sido("경기도")
        .sigungu("군포시")
        .today(dailyShortWeather)
        .build();
    List<S3FileDto> s3FileDtos = new ArrayList<>();
    for (int i = 1; i <= 3; i++) {
      S3FileDto s3FileDto = S3FileDto.builder()
          .url(Integer.toString(i))
          .build();
      s3FileDtos.add(s3FileDto);
    }

    given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
    given(locationRepository.findBySidoAndSigungu(anyString(), anyString()))
        .willReturn(Optional.of(location));
    given(shortTermWeatherService.getWeatherFromApi(anyString(), anyString()))
        .willReturn(response);
    given(postRepository.save(any(Post.class))).will(returnsFirstArg());
    given(fileService.saveFile(any(List.class))).willReturn(s3FileDtos);
    //when
    postService.registerPost(request, createImageList(3), 1L);

    //then
    ArgumentCaptor<Post> postArgumentCaptor = ArgumentCaptor.forClass(Post.class);
    ArgumentCaptor<Image> imageArgumentCaptor = ArgumentCaptor.forClass(Image.class);

    verify(postRepository).save(postArgumentCaptor.capture());
    verify(imageRepository, times(3)).save(imageArgumentCaptor.capture());

    assertEquals(request.getContent(), postArgumentCaptor.getValue().getText());
    assertEquals("경기도", postArgumentCaptor.getValue().getLocation().getSido());
    assertEquals("군포시", postArgumentCaptor.getValue().getLocation().getSigungu());
    assertEquals(9, postArgumentCaptor.getValue().getMinTemperature());
    assertEquals(12, postArgumentCaptor.getValue().getMaxTemperature());
    assertEquals("꼬북이", postArgumentCaptor.getValue().getMember().getNickName());

    assertEquals("1", imageArgumentCaptor.getAllValues().get(0).getUrl());
    assertEquals("2", imageArgumentCaptor.getAllValues().get(1).getUrl());
    assertEquals("3", imageArgumentCaptor.getAllValues().get(2).getUrl());
    assertEquals(postArgumentCaptor.getValue(),
        imageArgumentCaptor.getAllValues().get(0).getPost());
    assertEquals(postArgumentCaptor.getValue(),
        imageArgumentCaptor.getAllValues().get(1).getPost());
    assertEquals(postArgumentCaptor.getValue(),
        imageArgumentCaptor.getAllValues().get(2).getPost());
  }

  @Test
  void registerPost_shouldThrowMemberNotFound_whenMemberIsNotExist() {
    //given
    given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

    //when
    CustomException customException = assertThrows(CustomException.class,
        () -> postService.registerPost(request, createImageList(3), 1L));

    //then
    assertEquals(MEMBER_NOT_FOUND, customException.getErrorCode());
  }

  @Test
  void registerPost_shouldThrowNotFoundLocation_whenLocationIsNotExist() {
    //given
    Member member = Member.builder()
        .id(1L)
        .nickName("꼬북이")
        .build();

    given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
    given(locationRepository.findBySidoAndSigungu(anyString(), anyString()))
        .willReturn(Optional.empty());

    //when
    CustomException customException = assertThrows(CustomException.class,
        () -> postService.registerPost(request, createImageList(3), 1L));

    //then
    assertEquals(NOT_FOUND_LOCATION, customException.getErrorCode());
  }

  private List<MultipartFile> createImageList(int size) throws IOException {
    List<MultipartFile> images = new ArrayList<>();
    for (int i = 1; i <= size; i++) {
      images.add(MockDataUtil.createMockMultipartFile("images",
          "src/test/resources/image/image" + i + ".png"));
    }
    return images;
  }

  @Test
  void successUpdatePost() throws URISyntaxException, IOException {
    //given
    Member member = Member.builder()
        .id(1L)
        .build();
    Post post = Post.builder()
        .id(14L)
        .member(member)
        .build();
    Location location = Location.builder()
        .sido("경기도")
        .sigungu("군포시")
        .build();
    DailyShortWeather dailyShortWeather = DailyShortWeather.builder()
        .date("20240313")
        .maxTemperature(12)
        .minTemperature(9)
        .build();
    ShortTermWeatherResponse response = ShortTermWeatherResponse.builder()
        .sido("경기도")
        .sigungu("군포시")
        .today(dailyShortWeather)
        .build();
    List<S3FileDto> s3FileDtos = new ArrayList<>();
    for (int i = 1; i <= 3; i++) {
      S3FileDto s3FileDto = S3FileDto.builder()
          .url(Integer.toString(i))
          .build();
      s3FileDtos.add(s3FileDto);
    }
    List<Image> images = new ArrayList<>();

    given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
    given(locationRepository.findBySidoAndSigungu(anyString(), anyString()))
        .willReturn(Optional.of(location));
    given(shortTermWeatherService.getWeatherFromApi(anyString(), anyString()))
        .willReturn(response);
    given(fileService.saveFile(any(List.class))).willReturn(s3FileDtos);
    given(imageRepository.findAllByPost(any(Post.class))).willReturn(images);
    //when
    postService.updatePost(request, createImageList(3), 14L, 1L);

    //then
    ArgumentCaptor<Post> postArgumentCaptor = ArgumentCaptor.forClass(Post.class);
    ArgumentCaptor<Image> imageArgumentCaptor = ArgumentCaptor.forClass(Image.class);
    ArgumentCaptor<List<Image>> listArgumentCaptor = ArgumentCaptor.forClass(List.class);

    verify(postRepository).save(postArgumentCaptor.capture());
    verify(imageRepository, times(3)).save(imageArgumentCaptor.capture());
    verify(fileService, times(1)).deleteImages(listArgumentCaptor.capture());
    verify(imageRepository, times(1)).deleteAll(listArgumentCaptor.capture());

    assertEquals(request.getContent(), postArgumentCaptor.getValue().getText());
    assertEquals("경기도", postArgumentCaptor.getValue().getLocation().getSido());
    assertEquals("군포시", postArgumentCaptor.getValue().getLocation().getSigungu());
    assertEquals(9, postArgumentCaptor.getValue().getMinTemperature());
    assertEquals(12, postArgumentCaptor.getValue().getMaxTemperature());

    assertEquals("1", imageArgumentCaptor.getAllValues().get(0).getUrl());
    assertEquals("2", imageArgumentCaptor.getAllValues().get(1).getUrl());
    assertEquals("3", imageArgumentCaptor.getAllValues().get(2).getUrl());
    assertEquals(postArgumentCaptor.getValue(),
        imageArgumentCaptor.getAllValues().get(0).getPost());
    assertEquals(postArgumentCaptor.getValue(),
        imageArgumentCaptor.getAllValues().get(1).getPost());
    assertEquals(postArgumentCaptor.getValue(),
        imageArgumentCaptor.getAllValues().get(2).getPost());

    assertTrue(listArgumentCaptor.getAllValues().get(0).isEmpty());
    assertTrue(listArgumentCaptor.getAllValues().get(1).isEmpty());
  }

  @Test
  void updatePost_shouldThrowPostNotFound_whenPostIsNotExist() {
    //given
    given(postRepository.findById(anyLong())).willReturn(Optional.empty());

    //when
    CustomException customException = assertThrows(CustomException.class,
        () -> postService.updatePost(request, createImageList(3), 1L, 1L));

    //then
    assertEquals(POST_NOT_FOUND, customException.getErrorCode());
  }

  @Test
  void updatePost_shouldThrowMemberIsNotWriter_whenMemberIsNotWriter()
      throws URISyntaxException {
    //given
    Member member = Member.builder()
        .id(1L)
        .build();
    Post post = Post.builder()
        .id(14L)
        .member(member)
        .build();

    given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
    //when
    CustomException customException = assertThrows(CustomException.class,
        () -> postService.updatePost(request, createImageList(3), 1L, 3L));

    //then
    assertEquals(MEMBER_IS_NOT_WRITER, customException.getErrorCode());
  }

  @Test
  void updatePost_shouldThrowNotFoundLocation_whenLocationIsNotExist() {
    //given
    Member member = Member.builder()
        .id(1L)
        .build();
    Post post = Post.builder()
        .id(14L)
        .member(member)
        .build();

    given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
    given(locationRepository.findBySidoAndSigungu(anyString(), anyString()))
        .willReturn(Optional.empty());

    //when
    CustomException customException = assertThrows(CustomException.class,
        () -> postService.updatePost(request, createImageList(3), 1L, 1L));

    //then
    assertEquals(NOT_FOUND_LOCATION, customException.getErrorCode());
  }
}