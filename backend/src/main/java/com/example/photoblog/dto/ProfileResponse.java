package com.example.photoblog.dto;

public record ProfileResponse(
    String name,
    String title,
    String bio,
    String avatarUrl,
    String email
) {
}
