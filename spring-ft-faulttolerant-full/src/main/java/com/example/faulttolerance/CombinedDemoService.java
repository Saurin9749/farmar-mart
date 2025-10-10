package com.example.faulttolerance;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

@Service
public class CombinedDemoService {

    @CircuitBreaker(name = "demoCB", fallbackMethod = "fallback")
    @Retry(name = "globalRetry")
    @Bulkhead(name = "demoBulkhead", type = io.github.resilience4j.bulkhead.Bulkhead.Type.SEMAPHORE)
    public String combined() {
        // simulate flakiness
        if (Math.random() < 0.6) {
            throw new RuntimeException("combined-failure");
        }
        return "combined-success";
    }

    public String fallback(Throwable t) {
        return "combined-fallback: " + t.getMessage();
    }
}
