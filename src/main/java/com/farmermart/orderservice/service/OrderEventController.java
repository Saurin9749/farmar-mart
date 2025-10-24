package com.farmermart.orderservice.service;


import com.farmermart.orderservice.dto.event.CustomerOrder;
import com.farmermart.orderservice.dto.event.OrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderEventController {

    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @PostMapping("/publish")
    public void publishOrderEvent(@RequestBody CustomerOrder customerOrder) {
        try {
            // Create Kafka event
            OrderEvent event = new OrderEvent();
            event.setOrder(customerOrder);
            event.setType("ORDER_CREATED");

            // Send event to Kafka topic
            kafkaTemplate.send("new-orders", event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}