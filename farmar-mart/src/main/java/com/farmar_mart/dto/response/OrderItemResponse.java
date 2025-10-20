package com.farmar_mart.dto.response;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class OrderItemResponse {
    private Long productId;
    private Long quantity;
    private Double price;

}
