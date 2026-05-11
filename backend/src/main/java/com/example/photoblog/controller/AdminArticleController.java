package com.example.photoblog.controller;

import com.example.photoblog.common.ApiResponse;
import com.example.photoblog.common.PageResult;
import com.example.photoblog.dto.ArticleRequest;
import com.example.photoblog.dto.ArticleView;
import com.example.photoblog.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/articles")
public class AdminArticleController {
  private final ArticleService articleService;

  public AdminArticleController(ArticleService articleService) {
    this.articleService = articleService;
  }

  @GetMapping
  public ApiResponse<PageResult<ArticleView>> list(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size) {
    return ApiResponse.ok(articleService.listAdmin(page, size));
  }

  @GetMapping("/{id}")
  public ApiResponse<ArticleView> get(@PathVariable Long id) {
    return ApiResponse.ok(articleService.getById(id));
  }

  @PostMapping
  public ApiResponse<ArticleView> create(@Valid @RequestBody ArticleRequest request) {
    return ApiResponse.ok(articleService.create(request));
  }

  @PutMapping("/{id}")
  public ApiResponse<ArticleView> update(@PathVariable Long id, @Valid @RequestBody ArticleRequest request) {
    return ApiResponse.ok(articleService.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ApiResponse<Void> delete(@PathVariable Long id) {
    articleService.delete(id);
    return ApiResponse.ok();
  }
}
