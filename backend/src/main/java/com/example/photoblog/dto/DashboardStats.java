package com.example.photoblog.dto;

public record DashboardStats(
    Long articleCount,
    Long draftCount,
    Long pendingCommentCount,
    Long imageCount
) {
}
