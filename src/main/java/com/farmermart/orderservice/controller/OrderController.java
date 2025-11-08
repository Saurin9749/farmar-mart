package com.farmermart.orderservice.controller;

import com.farmermart.orderservice.dto.requestDto.AddressRequest;
import com.farmermart.orderservice.dto.requestDto.OrderRequest;
import com.farmermart.orderservice.dto.responseDto.OrderResponse;
import com.farmermart.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest){
        OrderResponse response = orderService.createOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        OrderResponse response = orderService.getOrderById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        OrderResponse response = orderService.getOrderById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUser(@PathVariable Long userId) {
        List<OrderResponse> responses = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(responses);
    }


    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Map<String, Object>> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Order cancelled successfully");
        response.put("status", "CANCELLED");
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{orderId}/address")
    public ResponseEntity<String> editOrderAddress(@PathVariable Long orderId, @RequestBody AddressRequest newAddress){
        orderService.editOrderAddress(orderId, newAddress);
        return ResponseEntity.ok("Address edited successfully");
    }


    @PutMapping("/{id}/payment")
    public ResponseEntity<String> makePayment(@PathVariable Long id){
        String message = orderService.makePayment(id);
        return ResponseEntity.ok(message);
    }









}
