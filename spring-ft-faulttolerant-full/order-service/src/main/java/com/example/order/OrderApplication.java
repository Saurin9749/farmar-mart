package com.example.order;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@SpringBootApplication
@RestController
@RequestMapping("/orders")
@EnableFeignClients
@EnableDiscoveryClient
public class OrderApplication {

    private final RestTemplate restTemplate = new RestTemplate();
    public static final String INVENTORY = "inventoryService";

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    // Synchronous call with CircuitBreaker + Retry + fallback
    @GetMapping("/order/{id}")
    @CircuitBreaker(name = INVENTORY, fallbackMethod = "inventoryFallback")
    @Retry(name = INVENTORY)
    public String placeOrder(@PathVariable String id) {
        String url = "http://localhost:8092/inventory/stock/" + id;
        String stock = restTemplate.getForObject(url, String.class);
        return "order-" + id + " -> " + stock;
    }

    public String inventoryFallback(String id, Throwable ex) {
        return "order-" + id + " -> FALLBACK_STOCK";
    }

    // Async call demonstrating TimeLimiter + Bulkhead
    @GetMapping("/order/async/{id}")
    @TimeLimiter(name = INVENTORY)
    @Bulkhead(name = INVENTORY)
    public CompletableFuture<String> placeOrderAsync(@PathVariable String id) {
        return CompletableFuture.supplyAsync(() -> {
            String url = "http://localhost:8092/inventory/stock/" + id;
            String stock = restTemplate.getForObject(url, String.class);
            return "async-order-" + id + " -> " + stock;
        });
    }
}
