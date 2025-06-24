package com.example.knowledge.service;

import java.util.concurrent.TimeUnit;

public interface RedisDistributedLock {
    String tryLock(String key, long timeout, TimeUnit unit);
    void unlock(String key, String value);
} 