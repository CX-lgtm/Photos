package com.example.photoblog.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.photoblog.dto.CommentRequest;
import com.example.photoblog.dto.CommentUpdateRequest;
import com.example.photoblog.entity.Comment;
import com.example.photoblog.exception.BusinessException;
import com.example.photoblog.mapper.CommentMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
  private final CommentMapper commentMapper;

  public CommentService(CommentMapper commentMapper) {
    this.commentMapper = commentMapper;
  }

  public List<Comment> publicComments(Long articleId, int size) {
    var wrapper = Wrappers.<Comment>lambdaQuery()
        .eq(Comment::getApproved, true)
        .orderByDesc(Comment::getCreatedAt)
        .last("limit " + Math.min(Math.max(size, 1), 50));
    if (articleId != null) {
      wrapper.eq(Comment::getArticleId, articleId);
    }
    return commentMapper.selectList(wrapper);
  }

  public Comment create(CommentRequest request) {
    LocalDateTime now = LocalDateTime.now();
    Comment comment = new Comment();
    comment.setArticleId(request.articleId());
    comment.setAuthorName(request.authorName());
    comment.setAuthorEmail(request.authorEmail());
    comment.setContent(request.content());
    comment.setApproved(false);
    comment.setCreatedAt(now);
    comment.setUpdatedAt(now);
    commentMapper.insert(comment);
    return comment;
  }

  public List<Comment> adminComments() {
    return commentMapper.selectList(Wrappers.<Comment>lambdaQuery().orderByDesc(Comment::getCreatedAt));
  }

  public Comment update(Long id, CommentUpdateRequest request) {
    Comment comment = commentMapper.selectById(id);
    if (comment == null) {
      throw new BusinessException(404, "Comment not found");
    }
    if (request.approved() != null) {
      comment.setApproved(request.approved());
    }
    comment.setUpdatedAt(LocalDateTime.now());
    commentMapper.updateById(comment);
    return comment;
  }

  public void delete(Long id) {
    commentMapper.deleteById(id);
  }
}
