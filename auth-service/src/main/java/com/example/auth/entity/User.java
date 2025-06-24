package com.example.auth.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 用户实体类（认证服务使用）
 * 
 * @author chatbot
 * @since 2024-06-24
 */
@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private Set<String> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 