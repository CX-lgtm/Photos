package com.example.photoblog.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.photoblog.entity.ImageAsset;
import com.example.photoblog.exception.BusinessException;
import com.example.photoblog.mapper.ImageAssetMapper;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageStorageService {
  private final ImageAssetMapper imageAssetMapper;

  @Value("${app.upload-dir}")
  private String uploadDir;

  private Path uploadPath;

  public ImageStorageService(ImageAssetMapper imageAssetMapper) {
    this.imageAssetMapper = imageAssetMapper;
  }

  @PostConstruct
  public void init() throws IOException {
    uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
    Files.createDirectories(uploadPath);
  }

  public List<ImageAsset> list() {
    return imageAssetMapper.selectList(Wrappers.<ImageAsset>lambdaQuery().orderByDesc(ImageAsset::getCreatedAt));
  }

  public ImageAsset store(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new BusinessException(400, "Image file is required");
    }
    String contentType = file.getContentType();
    if (contentType == null || !contentType.toLowerCase(Locale.ROOT).startsWith("image/")) {
      throw new BusinessException(400, "Only image files are allowed");
    }
    String originalName = StringUtils.cleanPath(
        file.getOriginalFilename() == null ? "image" : file.getOriginalFilename());
    String extension = extensionOf(originalName);
    String fileName = UUID.randomUUID() + extension;
    Path target = uploadPath.resolve(fileName).normalize();
    try {
      Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException ex) {
      throw new BusinessException(500, "Image upload failed");
    }
    ImageAsset asset = new ImageAsset();
    asset.setFileName(fileName);
    asset.setOriginalName(originalName);
    asset.setUrl("/uploads/" + fileName);
    asset.setContentType(contentType);
    asset.setSizeBytes(file.getSize());
    asset.setCreatedAt(LocalDateTime.now());
    imageAssetMapper.insert(asset);
    return asset;
  }

  public void delete(Long id) {
    ImageAsset asset = imageAssetMapper.selectById(id);
    if (asset == null) {
      return;
    }
    try {
      Files.deleteIfExists(uploadPath.resolve(asset.getFileName()).normalize());
    } catch (IOException ex) {
      throw new BusinessException(500, "Image delete failed");
    }
    imageAssetMapper.deleteById(id);
  }

  private String extensionOf(String originalName) {
    int dot = originalName.lastIndexOf('.');
    if (dot < 0) {
      return "";
    }
    return originalName.substring(dot).toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9.]", "");
  }
}
