package com.example.photoblog.controller;

import com.example.photoblog.common.ApiResponse;
import com.example.photoblog.dto.CommentUpdateRequest;
import com.example.photoblog.entity.Comment;
import com.example.photoblog.service.CommentService;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/comments")
public class AdminCommentController {
  private final CommentService commentService;

  public AdminCommentController(CommentService commentService) {
    this.commentService = commentService;
  }

  @GetMapping
  public ApiResponse<List<Comment>> list() {
    return ApiResponse.ok(commentService.adminComments());
  }

  @PatchMapping("/{id}")
  public ApiResponse<Comment> update(@PathVariable Long id, @RequestBody CommentUpdateRequest request) {
    return ApiResponse.ok(commentService.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ApiResponse<Void> delete(@PathVariable Long id) {
    commentService.delete(id);
    return ApiResponse.ok();
  }
}
