package com.fighting.weatherdress.post.service;

import static com.fighting.weatherdress.global.type.ErrorCode.MEMBER_IS_NOT_WRITER;
import static com.fighting.weatherdress.global.type.ErrorCode.MEMBER_NOT_FOUND;
import static com.fighting.weatherdress.global.type.ErrorCode.NOT_FOUND_LOCATION;
import static com.fighting.weatherdress.global.type.ErrorCode.POST_NOT_FOUND;

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
import com.fighting.weatherdress.s3.service.S3FileService;
import com.fighting.weatherdress.weather.dto.ShortTermWeatherResponse;
import com.fighting.weatherdress.weather.service.ShortTermWeatherService;
import java.net.URISyntaxException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostService {

  private final ShortTermWeatherService shortTermWeatherService;
  private final PostRepository postRepository;
  private final MemberRepository memberRepository;
  private final S3FileService fileService;
  private final LocationRepository locationRepository;
  private final ImageRepository imageRepository;

  public void registerPost(PostRequest request, List<MultipartFile> images,
      long memberId) throws URISyntaxException {
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
    Location location = locationRepository.findBySidoAndSigungu(
            request.getLocation().getSido(), request.getLocation().getSigungu())
        .orElseThrow(() -> new CustomException(NOT_FOUND_LOCATION));

    ShortTermWeatherResponse weather = shortTermWeatherService.getWeatherFromApi(
        request.getLocation().getSido(),
        request.getLocation().getSigungu());

    Post post = Post.toEntity(request.getContent(), weather, member, location);
    Post savedPost = postRepository.save(post);
    saveImages(images, savedPost);

  }

  public void updatePost(PostRequest request, List<MultipartFile> images, long postId,
      long memberId) throws URISyntaxException {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(POST_NOT_FOUND));

    if (post.getMember().getId() != memberId) {
      throw new CustomException(MEMBER_IS_NOT_WRITER);
    }

    Location location = locationRepository.findBySidoAndSigungu(
            request.getLocation().getSido(), request.getLocation().getSigungu())
        .orElseThrow(() -> new CustomException(NOT_FOUND_LOCATION));
    ShortTermWeatherResponse weather = shortTermWeatherService.getWeatherFromApi(
        request.getLocation().getSido(),
        request.getLocation().getSigungu());

    post.updatePost(request.getContent(), weather, location);
    postRepository.save(post);

    List<Image> oldImages = post.getImages();
    saveImages(images, post);
    fileService.deleteImages(oldImages);
    imageRepository.deleteAll(oldImages);

  }



  private void saveImages(List<MultipartFile> images, Post post) {
    fileService.saveFile(images).forEach(s3FileDto -> {
      Image image = Image.toEntity(s3FileDto.getUrl(), post);
      imageRepository.save(image);
    });
  }

}
