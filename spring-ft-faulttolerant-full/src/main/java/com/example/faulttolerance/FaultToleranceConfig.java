package com.example.faulttolerance;

import io.github.resilience4j.bulkhead.ThreadPoolBulkheadConfig;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableRetry
public class FaultToleranceConfig {

    // Global Retry config used by @Retry(name="globalRetry") or programmatic calls
    @Bean
    public RetryConfig globalRetryConfig() {
        return RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(300))
                .build();
    }

    @Bean
    public RetryRegistry retryRegistry(RetryConfig config) {
        return RetryRegistry.of(config);
    }

    // Circuit breaker config: fail if more than 50% failures in 10 calls sliding window
    @Bean
    public CircuitBreakerConfig demoCircuitBreakerConfig() {
        return CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .slidingWindowSize(10)
                .waitDurationInOpenState(Duration.ofSeconds(5))
                .permittedNumberOfCallsInHalfOpenState(3)
                .slowCallDurationThreshold(Duration.ofSeconds(2))
                .build();
    }

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry(CircuitBreakerConfig config) {
        return CircuitBreakerRegistry.of(config);
    }

    // Rate limiter config: 5 permits per second with a timeout of 500ms
    @Bean
    public RateLimiterConfig demoRateLimiterConfig() {
        return RateLimiterConfig.custom()
                .limitForPeriod(5)
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .timeoutDuration(Duration.ofMillis(500))
                .build();
    }

    @Bean
    public RateLimiterRegistry rateLimiterRegistry(RateLimiterConfig config) {
        return RateLimiterRegistry.of(config);
    }

    // Time limiter - for async calls; set to 1 second
    @Bean
    public TimeLimiterConfig demoTimeLimiterConfig() {
        return TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(1))
                .build();
    }

    @Bean
    public TimeLimiterRegistry timeLimiterRegistry(TimeLimiterConfig config) {
        return TimeLimiterRegistry.of(config);
    }

    // ThreadPool bulkhead for isolating concurrent calls
    @Bean
    public ThreadPoolBulkheadConfig demoBulkheadConfig() {
        return ThreadPoolBulkheadConfig.custom()
                .maxThreadPoolSize(4)
                .coreThreadPoolSize(2)
                .queueCapacity(10)
                .build();
    }

    @Bean
    public ThreadPoolBulkheadRegistry threadPoolBulkheadRegistry(ThreadPoolBulkheadConfig config) {
        return ThreadPoolBulkheadRegistry.of(config);
    }

    // Simple in-memory cache to demonstrate cached fallback
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("items");
    }
}
