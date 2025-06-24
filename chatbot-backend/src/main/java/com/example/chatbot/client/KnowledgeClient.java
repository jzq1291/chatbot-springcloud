package com.example.chatbot.client;

import com.example.chatbot.dto.PageResponse;
import com.example.chatbot.entity.KnowledgeBase;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "knowledge-service")
public interface KnowledgeClient {
    @GetMapping("/api/knowledge")
    ResponseEntity<PageResponse<KnowledgeBase>> findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size);

    @GetMapping("/api/knowledge/search")
    ResponseEntity<PageResponse<KnowledgeBase>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size);

    @GetMapping("/api/knowledge/similar")
    ResponseEntity<List<KnowledgeBase>> searchSimilar(
            @RequestParam String query,
            @RequestParam(defaultValue = "5") int topK);

    @GetMapping("/api/knowledge/category/{category}")
    ResponseEntity<PageResponse<KnowledgeBase>> findByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size);

    @GetMapping("/api/knowledge/{id}")
    ResponseEntity<KnowledgeBase> findById(@PathVariable Long id);

    @PostMapping("/api/knowledge")
    ResponseEntity<KnowledgeBase> addKnowledge(@RequestBody KnowledgeBase knowledge);

    @PutMapping("/api/knowledge/{id}")
    ResponseEntity<KnowledgeBase> updateKnowledge(
            @PathVariable Long id,
            @RequestBody KnowledgeBase knowledge);

    @DeleteMapping("/api/knowledge/{id}")
    ResponseEntity<Void> deleteKnowledge(@PathVariable Long id);

    @PostMapping("/api/knowledge/batch-import")
    ResponseEntity<Void> batchImport(@RequestBody List<KnowledgeBase> knowledgeList);
} 