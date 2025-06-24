package com.example.user.entity;

import lombok.Getter;

/**
 * 用户角色枚举
 * 
 * @author chatbot
 * @since 2024-06-24
 */
@Getter
public enum UserRole {
    ROLE_ADMIN("管理员"),
    ROLE_USER("普通用户"),
    ROLE_KNOWLEDGEMANAGER("知识库管理员");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }
} 