package com.example.photoblog.controller;

import com.example.photoblog.common.ApiResponse;
import com.example.photoblog.common.PageResult;
import com.example.photoblog.dto.ArticleView;
import com.example.photoblog.dto.CommentRequest;
import com.example.photoblog.dto.ProfileResponse;
import com.example.photoblog.entity.Category;
import com.example.photoblog.entity.Comment;
import com.example.photoblog.entity.Tag;
import com.example.photoblog.service.ArticleService;
import com.example.photoblog.service.CommentService;
import com.example.photoblog.service.TaxonomyService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PublicController {
  private final ArticleService articleService;
  private final TaxonomyService taxonomyService;
  private final CommentService commentService;

  @Value("${app.admin.display-name:Your Name}")
  private String displayName;

  public PublicController(
      ArticleService articleService,
      TaxonomyService taxonomyService,
      CommentService commentService) {
    this.articleService = articleService;
    this.taxonomyService = taxonomyService;
    this.commentService = commentService;
  }

  @GetMapping("/articles")
  public ApiResponse<PageResult<ArticleView>> articles(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "9") int size,
      @RequestParam(required = false) String category,
      @RequestParam(required = false) String tag,
      @RequestParam(required = false) String keyword) {
    return ApiResponse.ok(articleService.listPublished(page, size, category, tag, keyword));
  }

  @GetMapping("/articles/{slug}")
  public ApiResponse<ArticleView> article(@PathVariable String slug) {
    return ApiResponse.ok(articleService.getPublishedBySlug(slug));
  }

  @GetMapping("/categories")
  public ApiResponse<List<Category>> categories() {
    return ApiResponse.ok(taxonomyService.categories());
  }

  @GetMapping("/tags")
  public ApiResponse<List<Tag>> tags() {
    return ApiResponse.ok(taxonomyService.tags());
  }

  @GetMapping("/comments")
  public ApiResponse<List<Comment>> comments(
      @RequestParam(required = false) Long articleId,
      @RequestParam(defaultValue = "20") int size) {
    return ApiResponse.ok(commentService.publicComments(articleId, size));
  }

  @PostMapping("/comments")
  public ApiResponse<Comment> createComment(@Valid @RequestBody CommentRequest request) {
    return ApiResponse.ok(commentService.create(request));
  }

  @GetMapping("/profile")
  public ApiResponse<ProfileResponse> profile() {
    return ApiResponse.ok(new ProfileResponse(
        displayName,
        "Photographer",
        "I photograph quiet light, passing weather, and ordinary places that become memorable through attention.",
        "https://cx-lgtm.github.io/photography-blog/cover_hu_94a463cb71fae456.jpg",
        "you@example.com"));
  }
}
