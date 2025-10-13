package com.example.inventory.faulttolerance;

import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class TimeLimiterDemoService {

    @TimeLimiter(name = "demoTimeLimiter", fallbackMethod = "timeLimiterFallback")
    public CompletableFuture<String> slowAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "slow-result";
        });
    }
    public CompletableFuture<String> timeLimiterFallback(Throwable t) {
        return CompletableFuture.completedFuture("TimeLimiter triggered: operation took too long!");
    }
}
