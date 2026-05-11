package com.example.photoblog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("articles")
public class Article {
  @TableId(type = IdType.AUTO)
  private Long id;
  private String title;
  private String slug;
  private String summary;
  private String coverUrl;
  private String content;
  private String status;
  private Long viewCount;
  private LocalDateTime publishedAt;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
