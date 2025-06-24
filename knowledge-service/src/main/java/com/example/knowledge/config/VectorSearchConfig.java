package com.example.knowledge.config;

import com.example.knowledge.service.impl.VectorSearchServiceImpl;
import io.milvus.client.MilvusServiceClient;
import io.milvus.param.ConnectParam;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VectorSearchConfig {
    @Value("${milvus.host}")
    private String host;
    
    @Value("${milvus.port}")
    private int port;
    
    private final VectorSearchServiceImpl vectorSearchService;
    
    public VectorSearchConfig(VectorSearchServiceImpl vectorSearchService) {
        this.vectorSearchService = vectorSearchService;
    }
    
    @Bean
    public MilvusServiceClient milvusClient() {
        ConnectParam connectParam = ConnectParam.newBuilder()
                .withHost(host)
                .withPort(port)
                .build();
        return new MilvusServiceClient(connectParam);
    }
    
    @PostConstruct
    public void init() {
        vectorSearchService.init();
    }
} 