package com.farmermart.orderservice.controller;

import com.farmermart.orderservice.dto.event.CustomerOrder;
import com.farmermart.orderservice.dto.event.OrderEvent;
import com.farmermart.orderservice.dto.requestDto.AddressRequest;
import com.farmermart.orderservice.dto.requestDto.OrderRequest;
import com.farmermart.orderservice.dto.responseDto.OrderResponse;
import com.farmermart.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
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

    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;


    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            // 1️⃣ Create order using the service layer
            OrderResponse response = orderService.createOrder(orderRequest);

            // 2️⃣ Prepare CustomerOrder for Kafka
            CustomerOrder customerOrder = new CustomerOrder();
            customerOrder.setOrderId(response.getOrderId());
            customerOrder.setQuantity(response.getQuantity());
            customerOrder.setAmount(response.getTotalPrice());
            customerOrder.setItems(response.getItems()); // map list if supported

            // 3️⃣ Send Kafka event
            OrderEvent event = new OrderEvent();
            event.setOrder(customerOrder);
            event.setType("ORDER_CREATED");
            kafkaTemplate.send("new-orders", event);

            // 4️⃣ Return API response
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace();

            // Return meaningful response in case of error
            OrderResponse errorResponse = new OrderResponse();
            errorResponse.setStatus("FAILED");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
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
