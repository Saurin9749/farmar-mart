package com.farmermart.orderservice.kafka;


import com.farmermart.orderservice.dto.event.OrderEvent;
import com.farmermart.orderservice.model.Order;
import com.farmermart.orderservice.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ReverseOrder {

    @Autowired
    private OrderRepository repository;

    @KafkaListener(topics = "reversed-orders", groupId = "orders-group")
    public void reverseOrder(String event) {
        System.out.println("Inside reverse order for order "+event);

        try {
            OrderEvent orderEvent = new ObjectMapper().readValue(event, OrderEvent.class);

            Optional<Order> order = repository.findById(orderEvent.getOrder().getOrderId());

            order.ifPresent(o -> {
                o.setStatus("FAILED");
                this.repository.save(o);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}