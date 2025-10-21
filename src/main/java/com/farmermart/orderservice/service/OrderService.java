package com.farmermart.orderservice.service;

import com.farmermart.orderservice.dto.requestDto.AddressRequest;
import com.farmermart.orderservice.dto.requestDto.OrderRequest;
import com.farmermart.orderservice.dto.responseDto.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
    OrderResponse getOrderById(Long id);
    List<OrderResponse> getOrdersByUser(Long userId);
    void cancelOrder(Long id);
    void editOrderAddress(Long id, AddressRequest newAddress);
    String makePayment(Long id);

}
