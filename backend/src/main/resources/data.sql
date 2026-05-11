INSERT IGNORE INTO categories (id, name, slug, description, sort_order)
VALUES
  (1, 'Portrait', 'portrait', 'People, gesture, and emotion.', 10),
  (2, 'City', 'city', 'Streets, architecture, and daily rhythm.', 20);

INSERT IGNORE INTO tags (id, name, slug, color)
VALUES
  (1, 'Light', 'light', '#2563eb'),
  (2, 'Travel', 'travel', '#0ea5e9'),
  (3, 'Editing', 'editing', '#38bdf8');

INSERT IGNORE INTO articles (
  id, title, slug, summary, cover_url, content, status, view_count, published_at
) VALUES (
  1,
  'Storm light by the lake',
  'storm-light',
  'A quiet sample entry for testing the new full-stack photography journal.',
  'https://cx-lgtm.github.io/photography-blog/storm-light/feature_hu_2f9113d86b1ea456.jpg',
  '<p>The storm arrived slowly, leaving enough time to watch the light move across the water. This article is seed content for the Vue and Spring Boot version of the blog.</p><p>Replace it from the admin editor with your own shooting notes, camera settings, image sequence, and post-processing thoughts.</p>',
  'PUBLISHED',
  0,
  '2026-05-08 22:21:00'
);

INSERT IGNORE INTO article_category (article_id, category_id) VALUES (1, 1);
INSERT IGNORE INTO article_tag (article_id, tag_id) VALUES (1, 1), (1, 3);

INSERT IGNORE INTO comments (id, article_id, author_name, author_email, content, approved)
VALUES
  (1, 1, 'Visitor', 'visitor@example.com', 'Beautiful atmosphere and color.', 1);
