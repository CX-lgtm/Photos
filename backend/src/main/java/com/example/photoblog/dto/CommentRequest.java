package com.example.photoblog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(
    Long articleId,
    @NotBlank @Size(max = 40) String authorName,
    @Email @Size(max = 120) String authorEmail,
    @NotBlank @Size(max = 1000) String content
) {
}
