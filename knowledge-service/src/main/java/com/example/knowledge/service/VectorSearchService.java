package com.example.knowledge.service;

import com.example.knowledge.entity.KnowledgeBase;
import java.util.List;

public interface VectorSearchService {
    List<KnowledgeBase> searchSimilar(String query, int topK);
    void indexDocument(KnowledgeBase knowledge);
    void indexDocuments(List<KnowledgeBase> knowledgeList);
    void deleteDocument(Long id);
    void updateDocument(KnowledgeBase knowledge);
} 