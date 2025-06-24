package com.example.chatbot.service;

import com.example.chatbot.dto.PageResponse;
import com.example.chatbot.entity.KnowledgeBase;
import com.example.chatbot.client.KnowledgeClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KnowledgeService {
    private final KnowledgeClient knowledgeClient;

    public PageResponse<KnowledgeBase> findAll(int page, int size) {
        return knowledgeClient.findAll(page, size).getBody();
    }

    public PageResponse<KnowledgeBase> search(String keyword, int page, int size) {
        return knowledgeClient.search(keyword, page, size).getBody();
    }

    public List<KnowledgeBase> searchSimilar(String query, int topK) {
        return knowledgeClient.searchSimilar(query, topK).getBody();
    }

    public PageResponse<KnowledgeBase> findByCategory(String category, int page, int size) {
        return knowledgeClient.findByCategory(category, page, size).getBody();
    }

    public KnowledgeBase findById(Long id) {
        return knowledgeClient.findById(id).getBody();
    }

    public KnowledgeBase addKnowledge(KnowledgeBase knowledge) {
        return knowledgeClient.addKnowledge(knowledge).getBody();
    }

    public KnowledgeBase updateKnowledge(Long id, KnowledgeBase knowledge) {
        return knowledgeClient.updateKnowledge(id, knowledge).getBody();
    }

    public void deleteKnowledge(Long id) {
        knowledgeClient.deleteKnowledge(id);
    }

    public void batchImport(List<KnowledgeBase> knowledgeList) {
        knowledgeClient.batchImport(knowledgeList);
    }

    public List<KnowledgeBase> findAllData() {
        return knowledgeClient.findAll(1, Integer.MAX_VALUE).getBody().getContent();
    }
} 