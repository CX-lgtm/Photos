package com.example.photoblog.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.UUID;
import org.springframework.util.StringUtils;

public final class SlugUtil {
  private SlugUtil() {
  }

  public static String toSlug(String input, String fallbackPrefix) {
    String source = StringUtils.hasText(input) ? input : fallbackPrefix;
    String normalized = Normalizer.normalize(source, Normalizer.Form.NFD)
        .replaceAll("\\p{M}", "")
        .toLowerCase(Locale.ROOT)
        .replaceAll("[^a-z0-9]+", "-")
        .replaceAll("(^-|-$)", "");
    if (normalized.isBlank()) {
      return fallbackPrefix + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
    return normalized;
  }
}
