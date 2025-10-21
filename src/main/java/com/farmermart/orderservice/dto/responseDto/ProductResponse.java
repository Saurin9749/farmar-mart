package com.farmermart.orderservice.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ProductResponse {
    private Long productId;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;



}
