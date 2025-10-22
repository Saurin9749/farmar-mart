package com.farmar_mart.feign;
import com.farmar_mart.dto.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service", url = "http://localhost:8083")
public interface ProductClient {
    @GetMapping("/api/products/{id}")
    ProductResponse getProductById(@PathVariable("id") Long id);

    @PutMapping("/api/products/{id}/stock")
    void updateStock(@PathVariable("id") Long id, @RequestParam("quantity") int quantity);
}
