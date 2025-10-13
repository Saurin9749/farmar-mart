package com.example.inventory.faulttolerance;

import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CacheFallbackDemoService {

    private int calls = 0;

    @Cacheable(cacheNames = "items", key = "'item-'+#id")
    @Retry(name = "cacheRetry", fallbackMethod = "fallbackGetItem")
    public String getItem(int id) {
        calls++;
        if (calls % 2 == 0) {
            throw new RuntimeException("downstream read error for id " + id);
        }
        return "item-" + id + "@call" + calls;
    }
    public String fallbackGetItem(int id, Throwable t) {
        return "cached/fallback-item-" + id;
    }
}
