package com.example.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.user.dto.PageResponse;
import com.example.user.entity.User;
import com.example.user.entity.UserRole;
import com.example.user.exception.BusinessException;
import com.example.user.exception.ErrorCode;
import com.example.user.mapper.UserMapper;
import com.example.user.mapper.UserRoleMapper;
import com.example.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 * 
 * @author chatbot
 * @since 2024-06-24
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PageResponse<User> getAllUsers(int page, int size) {
        Page<User> pageResult = userMapper.selectPage(new Page<>(page, size), null);
        pageResult.getRecords().forEach(this::loadUserRoles);
        return new PageResponse<>(
            pageResult.getRecords(),
            page,
            size,
            pageResult.getTotal()
        );
    }

    @Override
    public User getUserById(Long id) {
        User user = Optional.ofNullable(userMapper.selectById(id))
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        loadUserRoles(user);
        return user;
    }

    @Override
    @Transactional
    public User createUser(User user) {
        if (userMapper.existsByUsername(user.getUsername()) > 0) {
            throw new BusinessException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }
        if (userMapper.existsByEmail(user.getEmail()) > 0) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insert(user);
        
        // 保存用户角色
        if (user.getRoles() != null) {
            user.getRoles().forEach(role -> userRoleMapper.insertUserRole(user.getId(), role.name()));
        }
        
        return user;
    }

    @Override
    @Transactional
    public User updateUser(Long id, User user) {
        User existingUser = getUserById(id);
        
        // 检查用户名是否被其他用户使用
        if (!existingUser.getUsername().equals(user.getUsername()) && 
            userMapper.existsByUsername(user.getUsername()) > 0) {
            throw new BusinessException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }
        
        // 检查邮箱是否被其他用户使用
        if (!existingUser.getEmail().equals(user.getEmail()) && 
            userMapper.existsByEmail(user.getEmail()) > 0) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        
        user.setId(id);
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(existingUser.getPassword());
        }
        
        userMapper.updateById(user);
        
        // 更新用户角色
        if (user.getRoles() != null) {
            userRoleMapper.deleteUserRoles(id);
            user.getRoles().forEach(role -> userRoleMapper.insertUserRole(id, role.name()));
        }
        
        return getUserById(id);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRoleMapper.deleteUserRoles(id);
        userMapper.deleteById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        User user = userMapper.findByUsername(username);
        if (user != null) {
            loadUserRoles(user);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        User user = userMapper.findByEmail(email);
        if (user != null) {
            loadUserRoles(user);
        }
        return Optional.ofNullable(user);
    }

    /**
     * 加载用户角色
     */
    private void loadUserRoles(User user) {
        Set<String> roleNames = userRoleMapper.findRolesByUserId(user.getId());
        Set<UserRole> roles = roleNames.stream()
                .map(UserRole::valueOf)
                .collect(Collectors.toSet());
        user.setRoles(roles);
    }
} 