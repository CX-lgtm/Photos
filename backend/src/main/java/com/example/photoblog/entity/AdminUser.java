package com.example.photoblog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("admin_users")
public class AdminUser {
  @TableId(type = IdType.AUTO)
  private Long id;
  private String username;
  private String passwordHash;
  private String displayName;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
