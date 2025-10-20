package com.farmar_mart.agriculture_order.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderResponse {
    private Long orderId;
    private Long userId;
    private Double totalPrice;
    private String status;
    private AddressResponse address;
    private List<OrderItemResponse> items;
}
