package com.example.knowledge.service;

import com.example.knowledge.entity.KnowledgeBase;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

public interface RedisService {
    RedisTemplate<String, Object> getRedisTemplate();
    void saveDocToRedis(KnowledgeBase knowledge);
    void incrementKnowledgeScore(String knowledgeId);
    List<KnowledgeBase> searchKnowledge(List<String> keywords);
    List<KnowledgeBase> getHotKnowledge(List<String> searchTerms);
    void removeExpiredHotKnowledge();
    void deleteKnowledge(Long id);
} 