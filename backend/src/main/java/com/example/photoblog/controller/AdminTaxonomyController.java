package com.example.photoblog.controller;

import com.example.photoblog.common.ApiResponse;
import com.example.photoblog.dto.CategoryRequest;
import com.example.photoblog.dto.TagRequest;
import com.example.photoblog.entity.Category;
import com.example.photoblog.entity.Tag;
import com.example.photoblog.service.TaxonomyService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminTaxonomyController {
  private final TaxonomyService taxonomyService;

  public AdminTaxonomyController(TaxonomyService taxonomyService) {
    this.taxonomyService = taxonomyService;
  }

  @GetMapping("/categories")
  public ApiResponse<List<Category>> categories() {
    return ApiResponse.ok(taxonomyService.categories());
  }

  @PostMapping("/categories")
  public ApiResponse<Category> createCategory(@Valid @RequestBody CategoryRequest request) {
    return ApiResponse.ok(taxonomyService.createCategory(request));
  }

  @PutMapping("/categories/{id}")
  public ApiResponse<Category> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
    return ApiResponse.ok(taxonomyService.updateCategory(id, request));
  }

  @DeleteMapping("/categories/{id}")
  public ApiResponse<Void> deleteCategory(@PathVariable Long id) {
    taxonomyService.deleteCategory(id);
    return ApiResponse.ok();
  }

  @GetMapping("/tags")
  public ApiResponse<List<Tag>> tags() {
    return ApiResponse.ok(taxonomyService.tags());
  }

  @PostMapping("/tags")
  public ApiResponse<Tag> createTag(@Valid @RequestBody TagRequest request) {
    return ApiResponse.ok(taxonomyService.createTag(request));
  }

  @PutMapping("/tags/{id}")
  public ApiResponse<Tag> updateTag(@PathVariable Long id, @Valid @RequestBody TagRequest request) {
    return ApiResponse.ok(taxonomyService.updateTag(id, request));
  }

  @DeleteMapping("/tags/{id}")
  public ApiResponse<Void> deleteTag(@PathVariable Long id) {
    taxonomyService.deleteTag(id);
    return ApiResponse.ok();
  }
}
