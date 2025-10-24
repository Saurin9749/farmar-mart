package com.farmermart.orderservice.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long orderId;
    private Long userId;
    private Double totalPrice;           // total order amount
    private String status;               // CREATED, FAILED, etc.
    private LocalDateTime createdAt;     // timestamp
    private AddressResponse address;     // shipping address
    private List<OrderItemResponse> items; // ordered items
    private int quantity;                // total quantity

    public String Item;

    public double Amount;
}
