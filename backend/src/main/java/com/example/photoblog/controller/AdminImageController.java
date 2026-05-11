package com.example.photoblog.controller;

import com.example.photoblog.common.ApiResponse;
import com.example.photoblog.entity.ImageAsset;
import com.example.photoblog.service.ImageStorageService;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/images")
public class AdminImageController {
  private final ImageStorageService imageStorageService;

  public AdminImageController(ImageStorageService imageStorageService) {
    this.imageStorageService = imageStorageService;
  }

  @GetMapping
  public ApiResponse<List<ImageAsset>> list() {
    return ApiResponse.ok(imageStorageService.list());
  }

  @PostMapping
  public ApiResponse<ImageAsset> upload(@RequestParam("file") MultipartFile file) {
    return ApiResponse.ok(imageStorageService.store(file));
  }

  @DeleteMapping("/{id}")
  public ApiResponse<Void> delete(@PathVariable Long id) {
    imageStorageService.delete(id);
    return ApiResponse.ok();
  }
}
