package com.example.photoblog.dto;

import jakarta.validation.constraints.NotBlank;

public record TagRequest(
    @NotBlank String name,
    String slug,
    String color
) {
}
