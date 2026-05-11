package com.example.photoblog.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

public record ArticleRequest(
    @NotBlank String title,
    String slug,
    String summary,
    String coverUrl,
    @NotBlank String content,
    String status,
    LocalDateTime publishedAt,
    List<Long> categoryIds,
    List<Long> tagIds
) {
}
