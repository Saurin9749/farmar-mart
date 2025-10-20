package com.farmar_mart.agriculture_order.dto.request;

import com.farmar_mart.agriculture_order.model.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {
    private Long userId;
    private List<OrderItem> orderItems;
    private AddressRequest address;
}
