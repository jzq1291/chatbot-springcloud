package com.example.auth.service;

import com.example.auth.dto.AuthRequest;
import com.example.auth.dto.AuthResponse;

/**
 * 认证服务接口
 * 
 * @author chatbot
 * @since 2024-06-24
 */
public interface AuthService {
    
    /**
     * 用户登录认证
     */
    AuthResponse authenticate(AuthRequest request);
    
    /**
     * 用户注册
     */
    AuthResponse register(AuthRequest request);
    
    /**
     * 用户登出
     */
    void logout(String authHeader);
    
    /**
     * 验证token
     */
    boolean validateToken(String token);
} 