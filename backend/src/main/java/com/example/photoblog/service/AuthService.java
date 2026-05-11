package com.example.photoblog.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.photoblog.dto.LoginRequest;
import com.example.photoblog.dto.LoginResponse;
import com.example.photoblog.entity.AdminUser;
import com.example.photoblog.exception.BusinessException;
import com.example.photoblog.mapper.AdminUserMapper;
import com.example.photoblog.security.JwtService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final AdminUserMapper adminUserMapper;
  private final JwtService jwtService;

  public AuthService(AdminUserMapper adminUserMapper, JwtService jwtService) {
    this.adminUserMapper = adminUserMapper;
    this.jwtService = jwtService;
  }

  public LoginResponse login(LoginRequest request) {
    AdminUser user = adminUserMapper.selectOne(
        Wrappers.<AdminUser>lambdaQuery().eq(AdminUser::getUsername, request.username()));
    if (user == null || !BCrypt.checkpw(request.password(), user.getPasswordHash())) {
      throw new BusinessException(401, "Invalid username or password");
    }
    return new LoginResponse(
        jwtService.createToken(user.getId(), user.getUsername()),
        user.getUsername(),
        user.getDisplayName());
  }
}
