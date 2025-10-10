package com.example.inventory.faulttolerance;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

@Service
public class CircuitBreakerDemoService {

    private int count = 0;

    @CircuitBreaker(name = "demoCB", fallbackMethod = "fallback")
    public String flakyOperation() {
        count++;
        if (count % 4 != 0) {
            throw new RuntimeException("intermittent failure #" + count);
        }
        return "success #" + count;
    }

    public String fallback(Throwable t) {
        return "cb-fallback: " + t.getMessage();
    }
}
