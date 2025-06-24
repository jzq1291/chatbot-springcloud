package com.example.user.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * 用户角色Mapper接口
 * 
 * @author chatbot
 * @since 2024-06-24
 */
@Mapper
public interface UserRoleMapper {
    
    /**
     * 插入用户角色
     */
    @Insert("INSERT INTO user_roles (user_id, role) VALUES (#{userId}, #{role})")
    void insertUserRole(Long userId, String role);

    /**
     * 删除用户角色
     */
    @Delete("DELETE FROM user_roles WHERE user_id = #{userId}")
    void deleteUserRoles(Long userId);

    /**
     * 根据用户ID查找角色
     */
    @Select("SELECT role FROM user_roles WHERE user_id = #{userId}")
    Set<String> findRolesByUserId(Long userId);
} 