package com.example.inventory.faulttolerance;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import org.springframework.stereotype.Service;

@Service
public class BulkheadDemoService {

    @Bulkhead(name = "demoBulkhead", type = io.github.resilience4j.bulkhead.Bulkhead.Type.THREADPOOL)
    public String protectedOperation() {
        // simulate some work that might block for a bit
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "bulkhead-response";
    }
}
