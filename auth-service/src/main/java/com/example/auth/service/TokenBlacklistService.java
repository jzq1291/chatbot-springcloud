package com.example.auth.service;

/**
 * Token黑名单服务接口
 * 
 * @author chatbot
 * @since 2024-06-24
 */
public interface TokenBlacklistService {
    
    /**
     * 将token添加到黑名单
     */
    void addToBlacklist(String token, long expirationTime);
    
    /**
     * 检查token是否在黑名单中
     */
    boolean isBlacklisted(String token);
} 