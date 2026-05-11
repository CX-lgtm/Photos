package com.example.photoblog.config;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.photoblog.entity.AdminUser;
import com.example.photoblog.mapper.AdminUserMapper;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class AdminBootstrap implements ApplicationRunner {
  private final AdminUserMapper adminUserMapper;

  @Value("${app.admin.username}")
  private String username;

  @Value("${app.admin.password}")
  private String password;

  @Value("${app.admin.display-name}")
  private String displayName;

  public AdminBootstrap(AdminUserMapper adminUserMapper) {
    this.adminUserMapper = adminUserMapper;
  }

  @Override
  public void run(ApplicationArguments args) {
    Long count = adminUserMapper.selectCount(
        Wrappers.<AdminUser>lambdaQuery().eq(AdminUser::getUsername, username));
    if (count > 0) {
      return;
    }
    LocalDateTime now = LocalDateTime.now();
    AdminUser user = new AdminUser();
    user.setUsername(username);
    user.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));
    user.setDisplayName(displayName);
    user.setCreatedAt(now);
    user.setUpdatedAt(now);
    adminUserMapper.insert(user);
  }
}
