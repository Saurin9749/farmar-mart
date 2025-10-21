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
    private Double totalPrice;
    private String status;
    private LocalDateTime createdAt;
    private AddressResponse address;
    private List<OrderItemResponse> items;
}
