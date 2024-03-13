package com.fighting.weatherdress.post.controller;

import com.fighting.weatherdress.post.dto.PostRequest;
import com.fighting.weatherdress.post.service.PostService;
import com.fighting.weatherdress.security.dto.CustomUserDetails;
import jakarta.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

  private final PostService postService;

  @PostMapping
  public void registerPost(@Valid @RequestPart PostRequest request,
      @RequestPart List<MultipartFile> images,
      @AuthenticationPrincipal CustomUserDetails userDetails) throws URISyntaxException {
    postService.registerPost(request, images, userDetails.getId());
  }

}
