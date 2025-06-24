package com.example.knowledge.mq;

import com.example.knowledge.config.RabbitMQConfig;
import com.example.knowledge.entity.KnowledgeBase;
import com.example.knowledge.mapper.KnowledgeBaseMapper;
import com.example.knowledge.service.VectorSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KnowledgeImportConsumer {
    private final KnowledgeBaseMapper knowledgeBaseMapper;
    private final VectorSearchService vectorSearchService;

    @RabbitListener(queues = RabbitMQConfig.KNOWLEDGE_IMPORT_QUEUE)
    @Transactional
    public void handleKnowledgeImport(List<KnowledgeBase> knowledgeList) {
        try {
            log.info("Processing batch import of {} knowledge items", knowledgeList.size());
            
            // 1. 保存到数据库
            for (KnowledgeBase knowledge : knowledgeList) {
                knowledgeBaseMapper.insert(knowledge);
            }
            
            // 2. 创建向量索引
            vectorSearchService.indexDocuments(knowledgeList);
            
            log.info("Successfully processed batch import of {} knowledge items", knowledgeList.size());
        } catch (Exception e) {
            log.error("Error processing knowledge import batch", e);
            throw e;
        }
    }
} 