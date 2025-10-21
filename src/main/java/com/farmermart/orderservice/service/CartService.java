package com.farmermart.orderservice.service;

import com.farmermart.orderservice.dto.requestDto.CartBulkRequest;
import com.farmermart.orderservice.dto.requestDto.CartRequest;
import com.farmermart.orderservice.dto.responseDto.CartResponse;

import java.util.List;

public interface CartService {
    CartResponse addToCart(CartRequest request);
    CartResponse removeFromCart(CartRequest request);
    List<CartResponse> getUserCart(Long userId);
    void clearCart(Long userId);

    //pooja
    List<CartResponse> addMultipleItems(CartBulkRequest request);

}
