package com.example.photoblog.dto;

import com.example.photoblog.entity.Category;
import com.example.photoblog.entity.Tag;
import java.time.LocalDateTime;
import java.util.List;

public record ArticleView(
    Long id,
    String title,
    String slug,
    String summary,
    String coverUrl,
    String content,
    String status,
    Long viewCount,
    LocalDateTime publishedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<Category> categories,
    List<Tag> tags,
    Long commentCount
) {
}
