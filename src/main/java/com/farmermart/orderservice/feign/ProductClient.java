package com.farmermart.orderservice.feign;

import com.farmermart.orderservice.dto.responseDto.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductClient {

    public ProductResponse getProduct(Long productId) {
    if (productId == 1L) return new ProductResponse(1L, "Product A", "Description A", 200.0, 10);
    if (productId == 2L) return new ProductResponse(2L, "Product B", "Description B", 350.0, 5);
    return new ProductResponse(3L, "Product C", "Description C", 150.0, 20);
}


}
