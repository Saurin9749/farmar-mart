package com.example.faulttolerance;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import org.springframework.stereotype.Service;

@Service
public class FaultTolerantDemo {

    @CircuitBreaker(name = "demoCB", fallbackMethod = "fallback")
    @Retry(name = "globalRetry")
    @Bulkhead(name = "demoBulkhead", type = Bulkhead.Type.THREADPOOL)
    public String unstableOperation() {
        // simulate unstable operation - in real project replace with remote call
        if (Math.random() < 0.7) {
            throw new RuntimeException("simulated failure");
        }
        return "success";
    }

    public String fallback(Throwable t) {
        return "fallback-response: " + t.getMessage();
    }
}
