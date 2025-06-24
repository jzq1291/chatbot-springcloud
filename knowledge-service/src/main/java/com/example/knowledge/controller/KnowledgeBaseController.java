package com.example.knowledge.controller;

import com.example.knowledge.dto.PageResponse;
import com.example.knowledge.entity.KnowledgeBase;
import com.example.knowledge.service.KnowledgeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/knowledge")
@RequiredArgsConstructor
public class KnowledgeBaseController {
    private final KnowledgeService knowledgeService;

    @GetMapping
    public ResponseEntity<PageResponse<KnowledgeBase>> findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(knowledgeService.findAll(page - 1, size));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<KnowledgeBase>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(knowledgeService.search(keyword, page - 1, size));
    }

    @GetMapping("/similar")
    public ResponseEntity<List<KnowledgeBase>> searchSimilar(
            @RequestParam String query,
            @RequestParam(defaultValue = "5") int topK) {
        return ResponseEntity.ok(knowledgeService.searchSimilar(query, topK));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<PageResponse<KnowledgeBase>> findByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(knowledgeService.findByCategory(category, page - 1, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<KnowledgeBase> findById(@PathVariable Long id) {
        return ResponseEntity.ok(knowledgeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<KnowledgeBase> addKnowledge(@RequestBody KnowledgeBase knowledge) {
        return ResponseEntity.ok(knowledgeService.addKnowledge(knowledge));
    }

    @PutMapping("/{id}")
    public ResponseEntity<KnowledgeBase> updateKnowledge(
            @PathVariable Long id,
            @RequestBody KnowledgeBase knowledge) {
        return ResponseEntity.ok(knowledgeService.updateKnowledge(id, knowledge));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKnowledge(@PathVariable Long id) {
        knowledgeService.deleteKnowledge(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/batch-import")
    public ResponseEntity<Void> batchImport(@RequestBody List<KnowledgeBase> knowledgeList) {
        log.info("Received batch import request with {} items", knowledgeList.size());
        
        if (knowledgeList.isEmpty()) {
            log.warn("Batch import request received with empty list");
            return ResponseEntity.badRequest().build();
        }
        
        if (knowledgeList.size() > 1000) {
            log.warn("Batch import request exceeded maximum limit of 1000 items. Received: {}", knowledgeList.size());
            return ResponseEntity.badRequest().build();
        }
        
        try {
            knowledgeService.batchImport(knowledgeList);
            log.info("Successfully queued batch import request for processing");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error processing batch import request", e);
            return ResponseEntity.internalServerError().build();
        }
    }
} 