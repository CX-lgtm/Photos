package com.example.photoblog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("comments")
public class Comment {
  @TableId(type = IdType.AUTO)
  private Long id;
  private Long articleId;
  private String authorName;
  private String authorEmail;
  private String content;
  private Boolean approved;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
