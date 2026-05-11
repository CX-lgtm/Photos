package com.example.photoblog.dto;

public record LoginResponse(
    String token,
    String username,
    String displayName
) {
}
