package com.example.photoblog.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.photoblog.common.ApiResponse;
import com.example.photoblog.dto.DashboardStats;
import com.example.photoblog.entity.Article;
import com.example.photoblog.entity.Comment;
import com.example.photoblog.mapper.ArticleMapper;
import com.example.photoblog.mapper.CommentMapper;
import com.example.photoblog.mapper.ImageAssetMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardController {
  private final ArticleMapper articleMapper;
  private final CommentMapper commentMapper;
  private final ImageAssetMapper imageAssetMapper;

  public AdminDashboardController(
      ArticleMapper articleMapper,
      CommentMapper commentMapper,
      ImageAssetMapper imageAssetMapper) {
    this.articleMapper = articleMapper;
    this.commentMapper = commentMapper;
    this.imageAssetMapper = imageAssetMapper;
  }

  @GetMapping
  public ApiResponse<DashboardStats> stats() {
    return ApiResponse.ok(new DashboardStats(
        articleMapper.selectCount(null),
        articleMapper.selectCount(Wrappers.<Article>lambdaQuery().eq(Article::getStatus, "DRAFT")),
        commentMapper.selectCount(Wrappers.<Comment>lambdaQuery().eq(Comment::getApproved, false)),
        imageAssetMapper.selectCount(null)));
  }
}
