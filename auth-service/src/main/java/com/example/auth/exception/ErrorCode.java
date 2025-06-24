package com.example.auth.exception;

import lombok.Getter;

/**
 * 错误码枚举
 * 
 * @author chatbot
 * @since 2024-06-24
 */
@Getter
public enum ErrorCode {
    // 用户相关错误码
    USER_NOT_FOUND(404, "用户不存在"),
    USERNAME_ALREADY_EXISTS(400, "用户名已存在"),
    EMAIL_ALREADY_EXISTS(400, "邮箱已存在"),
    INVALID_CREDENTIALS(401, "用户名或密码错误"),
    
    // 认证相关错误码
    INVALID_TOKEN(401, "无效的token"),
    TOKEN_EXPIRED(401, "token已过期"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    
    // 通用错误码
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    INVALID_PARAMETER(400, "参数错误");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
} 