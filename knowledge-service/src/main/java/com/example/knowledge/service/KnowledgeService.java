package com.example.knowledge.service;

import com.example.knowledge.dto.PageResponse;
import com.example.knowledge.entity.KnowledgeBase;

import java.util.List;

public interface KnowledgeService {
    PageResponse<KnowledgeBase> findAll(int page, int size);
    PageResponse<KnowledgeBase> search(String keyword, int page, int size);
    List<KnowledgeBase> searchSimilar(String query, int topK);
    PageResponse<KnowledgeBase> findByCategory(String category, int page, int size);
    KnowledgeBase findById(Long id);
    KnowledgeBase addKnowledge(KnowledgeBase knowledge);
    KnowledgeBase updateKnowledge(Long id, KnowledgeBase knowledge);
    void deleteKnowledge(Long id);
    void batchImport(List<KnowledgeBase> knowledgeList);
    List<KnowledgeBase> findAllData();
} 