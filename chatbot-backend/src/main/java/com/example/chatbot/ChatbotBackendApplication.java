package com.example.chatbot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.example.chatbot.mapper")
public class ChatbotBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatbotBackendApplication.class, args);
    }

}
