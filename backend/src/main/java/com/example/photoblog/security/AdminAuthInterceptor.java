package com.example.photoblog.security;

import com.example.photoblog.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {
  private final JwtService jwtService;

  public AdminAuthInterceptor(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
      return true;
    }
    String authorization = request.getHeader("Authorization");
    if (authorization == null || !authorization.startsWith("Bearer ")) {
      throw new BusinessException(401, "Login required");
    }
    try {
      var claims = jwtService.parse(authorization.substring(7));
      request.setAttribute("adminId", claims.get("adminId"));
      request.setAttribute("username", claims.getSubject());
      return true;
    } catch (Exception ex) {
      throw new BusinessException(401, "Invalid or expired token");
    }
  }
}
