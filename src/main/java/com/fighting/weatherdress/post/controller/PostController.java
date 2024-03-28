package com.fighting.weatherdress.post.controller;

import com.fighting.weatherdress.post.dto.PostRequest;
import com.fighting.weatherdress.post.dto.PostResponse;
import com.fighting.weatherdress.post.service.PostService;
import com.fighting.weatherdress.security.dto.CustomUserDetails;
import jakarta.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
      List<MultipartFile> images, @AuthenticationPrincipal CustomUserDetails userDetails)
      throws URISyntaxException {
    postService.registerPost(request, images, userDetails.getId());
  }

  @PutMapping("/{postId}")
  public void updatePost(@Valid @RequestPart PostRequest request,
      List<MultipartFile> images, @PathVariable long postId,
      @AuthenticationPrincipal CustomUserDetails userDetails) throws URISyntaxException {
    postService.updatePost(request, images, postId, userDetails.getId());
  }

  @GetMapping("/{postId}")
  public PostResponse getPost(@PathVariable long postId) {
    return postService.getPost(postId);
  }

  @GetMapping("/list")
  public Slice<PostResponse> getPostList(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "desc") String direction,
      @RequestParam(defaultValue = "createdAt") String sortBy) {
    PageRequest pageRequest = PageRequest.of(page, size, Direction.fromString(direction),
        sortBy);
    return postService.getPostList(pageRequest);
  }

}
