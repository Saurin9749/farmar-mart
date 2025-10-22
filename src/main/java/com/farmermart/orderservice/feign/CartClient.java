package com.farmermart.orderservice.feign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cart-service")
public interface CartClient {

    @DeleteMapping("/api/cart/{userId}/items/{productId}")
    void removeItemFromCart(@PathVariable("userId") Long userId, @PathVariable("productId") Long productId);
}
