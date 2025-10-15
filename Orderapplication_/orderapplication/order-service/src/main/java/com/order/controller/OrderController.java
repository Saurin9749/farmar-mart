package com.order.controller;

import com.order.model.Order;
import com.order.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public String placeOrder(@RequestBody Order order) {
        service.placeOrder(order);
        return "Order placed";
    }

    @GetMapping
    public Object list() {
        return service.getClass().getName();
    }
}
