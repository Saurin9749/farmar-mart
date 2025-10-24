package com.farmermart.orderservice.dto.event;


import com.farmermart.orderservice.dto.responseDto.OrderItemResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomerOrder {
    private String item;

    private int quantity;

    private double amount;

    private String paymentMode;

    private long orderId;

    private String address;

    public void setItems(List<OrderItemResponse> items) {

    }
}