package com.farmermart.orderservice.dto.requestDto;

import lombok.*;

import java.util.List;

//pooja
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartBulkRequest {
    private Long userId;
    private List<CartItemRequest> items;

    // getters and setters
}