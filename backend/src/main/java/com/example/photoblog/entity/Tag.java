package com.example.photoblog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("tags")
public class Tag {
  @TableId(type = IdType.AUTO)
  private Long id;
  private String name;
  private String slug;
  private String color;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
