package com.example.faulttolerance;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterDemoService {

    @RateLimiter(name = "demoRateLimiter", fallbackMethod = "fallback")
    public String limited() {
        // trivial fast operation; rate limiter will throttle calls
        return "rate-limited-response at " + System.currentTimeMillis();
    }

    public String fallback(Throwable t) {
        return "rate-limiter-fallback: " + t.getMessage();
    }
}
