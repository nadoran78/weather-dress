package com.fighting.weatherdress.post.dto;

import com.fighting.weatherdress.post.entity.Image;
import com.fighting.weatherdress.post.entity.Post;
import com.fighting.weatherdress.security.dto.MemberInfoDto;
import com.fighting.weatherdress.weather.dto.LocationDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PostResponse {

  private final Long id;

  private final String text;

  private final Integer minTemperature;

  private final Integer maxTemperature;

  private final Integer averageTemperature;

  private final Long likeCount;

  private final MemberInfoDto member;

  private final LocationDto location;

  private final List<String> imageUrls;

  public static PostResponse fromEntity(Post post) {
    int averageTemperature = (post.getMaxTemperature() + post.getMinTemperature()) / 2;
    return PostResponse.builder()
        .id(post.getId())
        .text(post.getText())
        .minTemperature(post.getMinTemperature())
        .maxTemperature(post.getMaxTemperature())
        .averageTemperature(averageTemperature)
        .likeCount(post.getLikeCount())
        .member(MemberInfoDto.fromEntity(post.getMember()))
        .location(LocationDto.builder()
            .sido(post.getLocation().getSido())
            .sigungu(post.getLocation().getSigungu())
            .build())
        .imageUrls(getImages(post.getImages()))
        .build();
  }

  private static List<String> getImages(List<Image> images) {
    return images == null ? null : images.stream().map(Image::getUrl).collect(
        Collectors.toList());
  }

}
