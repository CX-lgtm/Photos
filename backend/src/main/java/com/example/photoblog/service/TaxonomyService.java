package com.example.photoblog.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.photoblog.dto.CategoryRequest;
import com.example.photoblog.dto.TagRequest;
import com.example.photoblog.entity.Category;
import com.example.photoblog.entity.Tag;
import com.example.photoblog.exception.BusinessException;
import com.example.photoblog.mapper.CategoryMapper;
import com.example.photoblog.mapper.TagMapper;
import com.example.photoblog.util.SlugUtil;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TaxonomyService {
  private final CategoryMapper categoryMapper;
  private final TagMapper tagMapper;

  public TaxonomyService(CategoryMapper categoryMapper, TagMapper tagMapper) {
    this.categoryMapper = categoryMapper;
    this.tagMapper = tagMapper;
  }

  public List<Category> categories() {
    return categoryMapper.selectList(
        Wrappers.<Category>lambdaQuery().orderByAsc(Category::getSortOrder).orderByAsc(Category::getId));
  }

  public Category createCategory(CategoryRequest request) {
    LocalDateTime now = LocalDateTime.now();
    Category category = new Category();
    category.setName(request.name());
    category.setSlug(uniqueCategorySlug(SlugUtil.toSlug(slugSource(request.slug(), request.name()), "category"), null));
    category.setDescription(request.description());
    category.setSortOrder(request.sortOrder() == null ? 0 : request.sortOrder());
    category.setCreatedAt(now);
    category.setUpdatedAt(now);
    categoryMapper.insert(category);
    return category;
  }

  public Category updateCategory(Long id, CategoryRequest request) {
    Category category = categoryMapper.selectById(id);
    if (category == null) {
      throw new BusinessException(404, "Category not found");
    }
    category.setName(request.name());
    category.setSlug(uniqueCategorySlug(SlugUtil.toSlug(slugSource(request.slug(), request.name()), "category"), id));
    category.setDescription(request.description());
    category.setSortOrder(request.sortOrder() == null ? 0 : request.sortOrder());
    category.setUpdatedAt(LocalDateTime.now());
    categoryMapper.updateById(category);
    return category;
  }

  public void deleteCategory(Long id) {
    categoryMapper.deleteById(id);
  }

  public List<Tag> tags() {
    return tagMapper.selectList(Wrappers.<Tag>lambdaQuery().orderByAsc(Tag::getName));
  }

  public Tag createTag(TagRequest request) {
    LocalDateTime now = LocalDateTime.now();
    Tag tag = new Tag();
    tag.setName(request.name());
    tag.setSlug(uniqueTagSlug(SlugUtil.toSlug(slugSource(request.slug(), request.name()), "tag"), null));
    tag.setColor(StringUtils.hasText(request.color()) ? request.color() : "#2563eb");
    tag.setCreatedAt(now);
    tag.setUpdatedAt(now);
    tagMapper.insert(tag);
    return tag;
  }

  public Tag updateTag(Long id, TagRequest request) {
    Tag tag = tagMapper.selectById(id);
    if (tag == null) {
      throw new BusinessException(404, "Tag not found");
    }
    tag.setName(request.name());
    tag.setSlug(uniqueTagSlug(SlugUtil.toSlug(slugSource(request.slug(), request.name()), "tag"), id));
    tag.setColor(StringUtils.hasText(request.color()) ? request.color() : "#2563eb");
    tag.setUpdatedAt(LocalDateTime.now());
    tagMapper.updateById(tag);
    return tag;
  }

  public void deleteTag(Long id) {
    tagMapper.deleteById(id);
  }

  private String slugSource(String slug, String name) {
    return StringUtils.hasText(slug) ? slug : name;
  }

  private String uniqueCategorySlug(String base, Long currentId) {
    String candidate = base;
    int index = 2;
    while (categoryMapper.selectCount(Wrappers.<Category>lambdaQuery()
        .eq(Category::getSlug, candidate)
        .ne(currentId != null, Category::getId, currentId)) > 0) {
      candidate = base + "-" + index++;
    }
    return candidate;
  }

  private String uniqueTagSlug(String base, Long currentId) {
    String candidate = base;
    int index = 2;
    while (tagMapper.selectCount(Wrappers.<Tag>lambdaQuery()
        .eq(Tag::getSlug, candidate)
        .ne(currentId != null, Tag::getId, currentId)) > 0) {
      candidate = base + "-" + index++;
    }
    return candidate;
  }
}
