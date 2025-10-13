package com.example.inventory.faulttolerance;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.micrometer.tagged.TaggedCircuitBreakerMetrics;
import io.github.resilience4j.micrometer.tagged.TaggedBulkheadMetrics;
import io.github.resilience4j.micrometer.tagged.TaggedRetryMetrics;
import io.github.resilience4j.micrometer.tagged.TaggedTimeLimiterMetrics;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Resilience4jMetricsConfig {

    @Bean
    public TaggedCircuitBreakerMetrics circuitBreakerMetrics(CircuitBreakerRegistry registry) {
        return TaggedCircuitBreakerMetrics.ofCircuitBreakerRegistry(registry);
    }

    @Bean
    public TaggedBulkheadMetrics bulkheadMetrics(BulkheadRegistry registry) {
        return TaggedBulkheadMetrics.ofBulkheadRegistry(registry);
    }

    @Bean
    public TaggedRetryMetrics retryMetrics(RetryRegistry registry) {
        return TaggedRetryMetrics.ofRetryRegistry(registry);
    }

    @Bean
    public TaggedTimeLimiterMetrics timeLimiterMetrics(TimeLimiterRegistry registry) {
        return TaggedTimeLimiterMetrics.ofTimeLimiterRegistry(registry);
    }
}
