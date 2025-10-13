package com.example.inventory.faulttolerance;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

@Service
public class CombinedDemoService {

    @CircuitBreaker(name = "demoCB", fallbackMethod = "fallback")
    @Retry(name = "globalRetry")
    @Bulkhead(name = "demoBulkhead")
    public String combined() {
        if (Math.random() < 0.6) {
            throw new RuntimeException("combined-failure");
        }
        return "combined-success";
    }

    public String fallback(Throwable t) {
        return "combined-fallback: " + t.getMessage();
    }
}
