package com.example.knowledge.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String KNOWLEDGE_IMPORT_EXCHANGE = "knowledge.import.exchange";
    public static final String KNOWLEDGE_IMPORT_QUEUE = "knowledge.import.queue";
    public static final String KNOWLEDGE_IMPORT_ROUTING_KEY = "knowledge.import";

    @Bean
    public DirectExchange knowledgeImportExchange() {
        return new DirectExchange(KNOWLEDGE_IMPORT_EXCHANGE);
    }

    @Bean
    public Queue knowledgeImportQueue() {
        return new Queue(KNOWLEDGE_IMPORT_QUEUE);
    }

    @Bean
    public Binding knowledgeImportBinding() {
        return BindingBuilder.bind(knowledgeImportQueue())
                .to(knowledgeImportExchange())
                .with(KNOWLEDGE_IMPORT_ROUTING_KEY);
    }
} 