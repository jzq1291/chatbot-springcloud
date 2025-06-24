package com.example.knowledge.service.impl;

import com.example.knowledge.service.RedisDistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisDistributedLockImpl implements RedisDistributedLock {
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public String tryLock(String key, long timeout, TimeUnit unit) {
        String value = UUID.randomUUID().toString();
        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(key, value, timeout, unit);
        return Boolean.TRUE.equals(success) ? value : null;
    }

    @Override
    public void unlock(String key, String value) {
        String currentValue = (String) redisTemplate.opsForValue().get(key);
        if (value.equals(currentValue)) {
            redisTemplate.delete(key);
        }
    }
} 