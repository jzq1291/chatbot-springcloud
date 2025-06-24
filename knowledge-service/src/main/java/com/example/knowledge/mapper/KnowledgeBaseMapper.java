package com.example.knowledge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.knowledge.entity.KnowledgeBase;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface KnowledgeBaseMapper extends BaseMapper<KnowledgeBase> {
    @Select("SELECT * FROM knowledge_base WHERE title LIKE #{keyword} OR content LIKE #{keyword}")
    Page<KnowledgeBase> searchByKeyword(Page<KnowledgeBase> page, @Param("keyword") String keyword);

    @Select("SELECT * FROM knowledge_base WHERE category = #{category}")
    Page<KnowledgeBase> findByCategory(Page<KnowledgeBase> page, @Param("category") String category);

    @Select("SELECT * FROM knowledge_base WHERE id IN (#{ids})")
    List<KnowledgeBase> findByIds(@Param("ids") List<Long> ids);
} 