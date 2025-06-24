package com.example.auth.client;

import com.example.auth.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

/**
 * 用户服务客户端接口
 * 
 * @author chatbot
 * @since 2024-06-24
 */
@FeignClient(name = "user-service")
public interface UserClient {
    
    /**
     * 根据用户名查找用户
     */
    @GetMapping("/users/username/{username}")
    ResponseEntity<User> getUserByUsername(@PathVariable String username);
    
    /**
     * 根据邮箱查找用户
     */
    @GetMapping("/users/email/{email}")
    ResponseEntity<User> getUserByEmail(@PathVariable String email);
} 