package com.example.auth.dto;

import lombok.Data;

/**
 * 认证请求DTO
 * 
 * @author chatbot
 * @since 2024-06-24
 */
@Data
public class AuthRequest {
    private String username;
    private String password;
    private String email;
} 