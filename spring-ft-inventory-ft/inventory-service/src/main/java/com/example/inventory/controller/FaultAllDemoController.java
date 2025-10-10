package com.example.inventory.controller;

import com.example.inventory.faulttolerance.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class FaultAllDemoController {

    @Autowired
    RetryDemoService retrySvc;

    @Autowired
    CircuitBreakerDemoService cbSvc;

    @Autowired
    BulkheadDemoService bulkheadSvc;

    @Autowired
    RateLimiterDemoService rateSvc;

    @Autowired
    TimeLimiterDemoService timeSvc;

    @Autowired
    CacheFallbackDemoService cacheSvc;

    @Autowired
    CombinedDemoService combinedSvc;

    @GetMapping("/fault/retry")
    public String retry() {
        return retrySvc.unreliable();
    }

    @GetMapping("/fault/cb")
    public String cb() {
        return cbSvc.flakyOperation();
    }

    @GetMapping("/fault/bulk")
    public String bulk() {
        return bulkheadSvc.protectedOperation();
    }

    @GetMapping("/fault/rate")
    public String rate() {
        return rateSvc.limited();
    }

    @GetMapping("/fault/time")
    public CompletableFuture<String> time() {
        return timeSvc.slowAsync();
    }

    @GetMapping("/fault/cache/{id}")
    public String cache(@PathVariable int id) {
        return cacheSvc.getItem(id);
    }

    @GetMapping("/fault/combined")
    public String combined() {
        return combinedSvc.combined();
    }
}
