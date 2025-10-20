package com.farmar_mart.dto.response;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class OrderDetailsResponse {
    private Long orderId;
    private String status;
    private Double totalPrice;
    private UserResponse user;
    private List<ProductResponse> products;


}
