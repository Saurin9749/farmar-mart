package com.farmermart.orderservice.dto.requestDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartRequest {
    private Long userId;
    private Long productId;
    private Double productPrice;
    //pooja
    private String productName;
    private int quantity;

//    public CartRequest(Long userId, Long productId, String productName, Double price, int quantity) {
//    }
}
