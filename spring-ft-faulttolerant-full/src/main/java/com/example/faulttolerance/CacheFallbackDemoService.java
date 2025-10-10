package com.example.faulttolerance;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CacheFallbackDemoService {

    private int calls = 0;

    @Cacheable(cacheNames = "items", key = "'item-'+#id")
    public String getItem(int id) {
        calls++;
        if (calls % 2 == 0) {
            // simulate a failure that will be handled by previously cached value on alternate calls
            throw new RuntimeException("downstream read error for id " + id);
        }
        return "item-"+id+"@call"+calls;
    }
}
