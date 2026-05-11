package com.example.photoblog.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
    @NotBlank String name,
    String slug,
    String description,
    Integer sortOrder
) {
}
