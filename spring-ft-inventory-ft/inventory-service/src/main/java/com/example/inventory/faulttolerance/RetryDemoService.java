package com.example.inventory.faulttolerance;

import org.springframework.stereotype.Service;
import io.github.resilience4j.retry.annotation.Retry;

@Service
public class RetryDemoService {

    private int counter = 0;

    @Retry(name = "globalRetry")
    public String unreliable() {
        counter++;
        if (counter % 3 != 0) {
            throw new RuntimeException("transient error (call " + counter + ")");
        }
        return "reliable result at call " + counter;
    }
}
