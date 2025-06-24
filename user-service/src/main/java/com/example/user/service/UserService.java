package com.example.user.service;

import com.example.user.dto.PageResponse;
import com.example.user.entity.User;
import java.util.Optional;

/**
 * 用户服务接口
 * 
 * @author chatbot
 * @since 2024-06-24
 */
public interface UserService {
    
    /**
     * 分页获取所有用户
     */
    PageResponse<User> getAllUsers(int page, int size);
    
    /**
     * 根据ID获取用户
     */
    User getUserById(Long id);
    
    /**
     * 创建用户
     */
    User createUser(User user);
    
    /**
     * 更新用户
     */
    User updateUser(Long id, User user);
    
    /**
     * 删除用户
     */
    void deleteUser(Long id);
    
    /**
     * 根据用户名查找用户
     */
    Optional<User> findByUsername(String username);
    
    /**
     * 根据邮箱查找用户
     */
    Optional<User> findByEmail(String email);
} 