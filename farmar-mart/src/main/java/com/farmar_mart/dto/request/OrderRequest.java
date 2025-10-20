package com.farmar_mart.dto.request;
import com.farmar_mart.model.OrderItem;
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
