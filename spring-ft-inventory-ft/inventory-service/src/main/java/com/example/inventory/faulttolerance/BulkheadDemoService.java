package com.example.inventory.faulttolerance;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import org.springframework.stereotype.Service;

@Service
public class BulkheadDemoService {
    @Bulkhead(name = "demoBulkhead", type = Bulkhead.Type.THREADPOOL, fallbackMethod = "bulkheadFallback")
    public String protectedOperation() {
        try {
            // slow korlam
            Thread.sleep(30);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Processed by " + Thread.currentThread().getName();
    }
    public String bulkheadFallback(Throwable t) {
        return "Bulkhead busy! Please try again later.";
    }
}
