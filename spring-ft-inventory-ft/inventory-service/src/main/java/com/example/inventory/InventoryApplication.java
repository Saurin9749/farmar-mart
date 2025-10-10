package com.example.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
@RequestMapping("/inventory")
public class InventoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class, args);
    }

    @GetMapping("/stock/{id}")
    public String stock(@PathVariable String id) throws InterruptedException {
        // "slow" simulates latency; "down" throws exception to simulate failure
        if ("slow".equals(id)) {
            Thread.sleep(4000); // 4 seconds
        }
        if ("down".equals(id)) {
            throw new RuntimeException("inventory service failing");
        }
        return "stock-" + id;
    }
}
