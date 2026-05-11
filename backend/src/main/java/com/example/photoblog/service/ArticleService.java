package com.example.photoblog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.photoblog.common.PageResult;
import com.example.photoblog.dto.ArticleRequest;
import com.example.photoblog.dto.ArticleView;
import com.example.photoblog.entity.Article;
import com.example.photoblog.entity.ArticleCategory;
import com.example.photoblog.entity.ArticleTag;
import com.example.photoblog.entity.Category;
import com.example.photoblog.entity.Comment;
import com.example.photoblog.entity.Tag;
import com.example.photoblog.exception.BusinessException;
import com.example.photoblog.mapper.ArticleCategoryMapper;
import com.example.photoblog.mapper.ArticleMapper;
import com.example.photoblog.mapper.ArticleTagMapper;
import com.example.photoblog.mapper.CategoryMapper;
import com.example.photoblog.mapper.CommentMapper;
import com.example.photoblog.mapper.TagMapper;
import com.example.photoblog.util.SlugUtil;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class ArticleService {
  private final ArticleMapper articleMapper;
  private final CategoryMapper categoryMapper;
  private final TagMapper tagMapper;
  private final ArticleCategoryMapper articleCategoryMapper;
  private final ArticleTagMapper articleTagMapper;
  private final CommentMapper commentMapper;

  public ArticleService(
      ArticleMapper articleMapper,
      CategoryMapper categoryMapper,
      TagMapper tagMapper,
      ArticleCategoryMapper articleCategoryMapper,
      ArticleTagMapper articleTagMapper,
      CommentMapper commentMapper) {
    this.articleMapper = articleMapper;
    this.categoryMapper = categoryMapper;
    this.tagMapper = tagMapper;
    this.articleCategoryMapper = articleCategoryMapper;
    this.articleTagMapper = articleTagMapper;
    this.commentMapper = commentMapper;
  }

  public PageResult<ArticleView> listPublished(int page, int size, String categorySlug, String tagSlug, String keyword) {
    LambdaQueryWrapper<Article> wrapper = Wrappers.<Article>lambdaQuery()
        .eq(Article::getStatus, "PUBLISHED")
        .orderByDesc(Article::getPublishedAt)
        .orderByDesc(Article::getId);
    if (StringUtils.hasText(keyword)) {
      wrapper.and(query -> query.like(Article::getTitle, keyword).or().like(Article::getSummary, keyword));
    }
    if (StringUtils.hasText(categorySlug)) {
      wrapper.apply("exists (select 1 from article_category ac join categories c on c.id = ac.category_id where ac.article_id = articles.id and c.slug = {0})", categorySlug);
    }
    if (StringUtils.hasText(tagSlug)) {
      wrapper.apply("exists (select 1 from article_tag atg join tags t on t.id = atg.tag_id where atg.article_id = articles.id and t.slug = {0})", tagSlug);
    }
    IPage<Article> result = articleMapper.selectPage(new Page<>(safePage(page), safeSize(size)), wrapper);
    return toPageResult(result);
  }

  public PageResult<ArticleView> listAdmin(int page, int size) {
    IPage<Article> result = articleMapper.selectPage(
        new Page<>(safePage(page), safeSize(size)),
        Wrappers.<Article>lambdaQuery().orderByDesc(Article::getUpdatedAt).orderByDesc(Article::getId));
    return toPageResult(result);
  }

  public ArticleView getPublishedBySlug(String slug) {
    Article article = articleMapper.selectOne(
        Wrappers.<Article>lambdaQuery().eq(Article::getSlug, slug).eq(Article::getStatus, "PUBLISHED"));
    if (article == null) {
      throw new BusinessException(404, "Article not found");
    }
    articleMapper.update(null, Wrappers.<Article>lambdaUpdate()
        .eq(Article::getId, article.getId())
        .setSql("view_count = view_count + 1")
        .set(Article::getUpdatedAt, article.getUpdatedAt()));
    article.setViewCount((article.getViewCount() == null ? 0 : article.getViewCount()) + 1);
    return toView(article);
  }

  public ArticleView getById(Long id) {
    Article article = articleMapper.selectById(id);
    if (article == null) {
      throw new BusinessException(404, "Article not found");
    }
    return toView(article);
  }

  @Transactional
  public ArticleView create(ArticleRequest request) {
    Article article = new Article();
    LocalDateTime now = LocalDateTime.now();
    applyRequest(article, request);
    article.setSlug(uniqueArticleSlug(article.getSlug(), null));
    article.setViewCount(0L);
    article.setCreatedAt(now);
    article.setUpdatedAt(now);
    if ("PUBLISHED".equals(article.getStatus()) && article.getPublishedAt() == null) {
      article.setPublishedAt(now);
    }
    articleMapper.insert(article);
    replaceRelations(article.getId(), request.categoryIds(), request.tagIds());
    return toView(article);
  }

  @Transactional
  public ArticleView update(Long id, ArticleRequest request) {
    Article article = articleMapper.selectById(id);
    if (article == null) {
      throw new BusinessException(404, "Article not found");
    }
    LocalDateTime previousPublishedAt = article.getPublishedAt();
    applyRequest(article, request);
    if (request.publishedAt() == null) {
      article.setPublishedAt(previousPublishedAt);
    }
    article.setSlug(uniqueArticleSlug(article.getSlug(), id));
    article.setUpdatedAt(LocalDateTime.now());
    if ("PUBLISHED".equals(article.getStatus()) && article.getPublishedAt() == null) {
      article.setPublishedAt(LocalDateTime.now());
    }
    articleMapper.updateById(article);
    replaceRelations(id, request.categoryIds(), request.tagIds());
    return toView(article);
  }

  @Transactional
  public void delete(Long id) {
    articleCategoryMapper.delete(Wrappers.<ArticleCategory>lambdaQuery().eq(ArticleCategory::getArticleId, id));
    articleTagMapper.delete(Wrappers.<ArticleTag>lambdaQuery().eq(ArticleTag::getArticleId, id));
    commentMapper.delete(Wrappers.<Comment>lambdaQuery().eq(Comment::getArticleId, id));
    articleMapper.deleteById(id);
  }

  private void applyRequest(Article article, ArticleRequest request) {
    article.setTitle(request.title());
    article.setSlug(SlugUtil.toSlug(StringUtils.hasText(request.slug()) ? request.slug() : request.title(), "article"));
    article.setSummary(request.summary());
    article.setCoverUrl(request.coverUrl());
    article.setContent(request.content());
    article.setStatus("PUBLISHED".equalsIgnoreCase(request.status()) ? "PUBLISHED" : "DRAFT");
    article.setPublishedAt(request.publishedAt());
  }

  private void replaceRelations(Long articleId, List<Long> categoryIds, List<Long> tagIds) {
    articleCategoryMapper.delete(Wrappers.<ArticleCategory>lambdaQuery().eq(ArticleCategory::getArticleId, articleId));
    articleTagMapper.delete(Wrappers.<ArticleTag>lambdaQuery().eq(ArticleTag::getArticleId, articleId));
    if (categoryIds != null) {
      categoryIds.stream().filter(Objects::nonNull).distinct().forEach(categoryId -> {
        ArticleCategory relation = new ArticleCategory();
        relation.setArticleId(articleId);
        relation.setCategoryId(categoryId);
        articleCategoryMapper.insert(relation);
      });
    }
    if (tagIds != null) {
      tagIds.stream().filter(Objects::nonNull).distinct().forEach(tagId -> {
        ArticleTag relation = new ArticleTag();
        relation.setArticleId(articleId);
        relation.setTagId(tagId);
        articleTagMapper.insert(relation);
      });
    }
  }

  private ArticleView toView(Article article) {
    List<Category> categories = categoriesFor(article.getId());
    List<Tag> tags = tagsFor(article.getId());
    Long commentCount = commentMapper.selectCount(
        Wrappers.<Comment>lambdaQuery()
            .eq(Comment::getArticleId, article.getId())
            .eq(Comment::getApproved, true));
    return new ArticleView(
        article.getId(),
        article.getTitle(),
        article.getSlug(),
        article.getSummary(),
        article.getCoverUrl(),
        article.getContent(),
        article.getStatus(),
        article.getViewCount(),
        article.getPublishedAt(),
        article.getCreatedAt(),
        article.getUpdatedAt(),
        categories,
        tags,
        commentCount);
  }

  private List<Category> categoriesFor(Long articleId) {
    List<Long> ids = articleCategoryMapper.selectList(
            Wrappers.<ArticleCategory>lambdaQuery().eq(ArticleCategory::getArticleId, articleId))
        .stream()
        .map(ArticleCategory::getCategoryId)
        .toList();
    return ids.isEmpty() ? List.of() : categoryMapper.selectBatchIds(ids);
  }

  private List<Tag> tagsFor(Long articleId) {
    List<Long> ids = articleTagMapper.selectList(
            Wrappers.<ArticleTag>lambdaQuery().eq(ArticleTag::getArticleId, articleId))
        .stream()
        .map(ArticleTag::getTagId)
        .toList();
    return ids.isEmpty() ? List.of() : tagMapper.selectBatchIds(ids);
  }

  private PageResult<ArticleView> toPageResult(IPage<Article> result) {
    return new PageResult<>(
        result.getRecords().stream().map(this::toView).toList(),
        result.getTotal(),
        result.getCurrent(),
        result.getSize());
  }

  private String uniqueArticleSlug(String base, Long currentId) {
    return uniqueSlug(base, suffix -> articleMapper.selectCount(
        Wrappers.<Article>lambdaQuery()
            .eq(Article::getSlug, suffix)
            .ne(currentId != null, Article::getId, currentId)) == 0);
  }

  private String uniqueSlug(String base, java.util.function.Predicate<String> available) {
    String candidate = base;
    int index = 2;
    while (!available.test(candidate)) {
      candidate = base + "-" + index++;
    }
    return candidate;
  }

  private long safePage(int page) {
    return Math.max(page, 1);
  }

  private long safeSize(int size) {
    return Math.min(Math.max(size, 1), 50);
  }
}
