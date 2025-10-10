package com.example.faulttolerance;

import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class TimeLimiterDemoService {

    @TimeLimiter(name = "demoTimeLimiter")
    public CompletableFuture<String> slowAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1500); // intentionally longer than time limiter (1s)
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "slow-result";
        });
    }
}
