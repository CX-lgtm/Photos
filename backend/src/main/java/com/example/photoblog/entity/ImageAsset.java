package com.example.photoblog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("image_assets")
public class ImageAsset {
  @TableId(type = IdType.AUTO)
  private Long id;
  private String fileName;
  private String originalName;
  private String url;
  private String contentType;
  private Long sizeBytes;
  private LocalDateTime createdAt;
}
