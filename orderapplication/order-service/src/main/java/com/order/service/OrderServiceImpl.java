package com.order.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.model.Order;
import com.order.repository.OrderRepository;
import com.order.service.kafka.OrderProducer;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final OrderProducer producer;
    private final ObjectMapper mapper = new ObjectMapper();

    public OrderServiceImpl(OrderRepository repository, OrderProducer producer) {
        this.repository = repository;
        this.producer = producer;
    }

    @Override
    public void placeOrder(Order order) {
        repository.save(order);
        try {
            String json = mapper.writeValueAsString(order);
            producer.sendOrderEvent(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
