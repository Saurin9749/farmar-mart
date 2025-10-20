package com.farmar_mart.service;

import com.farmar_mart.dto.request.OrderRequest;
import com.farmar_mart.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderById(Long orderId);

    List<OrderResponse> getOrdersByUserId(Long userId);


    List<OrderResponse> getOrdersByStatus(String status);

    void cancelOrder(Long orderId);


}

