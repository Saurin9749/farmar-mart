package com.farmermart.orderservice.dto.requestDto;

import lombok.*;

//pooja

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequest {
    private Long productId;
    private String productName;
    private Double price;
    private int quantity;

    // getters and setters
}