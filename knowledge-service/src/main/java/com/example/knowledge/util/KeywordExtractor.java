package com.example.knowledge.util;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class KeywordExtractor {
    public List<String> extractKeywordsFromArticle(String text, int maxKeywords) {
        // 简单实现：按空格分词，取前N个词作为关键词
        // TODO: 后续可以接入专业的分词和关键词提取服务
        return Arrays.stream(text.split("\\s+"))
                .filter(word -> word.length() > 1)
                .distinct()
                .limit(maxKeywords)
                .collect(Collectors.toList());
    }
} 