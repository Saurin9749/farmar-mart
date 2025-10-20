package com.example.order.service;

import com.example.order.client.InventoryClient;
import com.example.order.dto.InventoryReserveRequest;
import com.example.order.model.Order;
import com.example.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final InventoryClient inventoryClient;
    private final OrderRepository orderRepository;

    public OrderService(InventoryClient inventoryClient, OrderRepository orderRepository) {
        this.inventoryClient = inventoryClient;
        this.orderRepository = orderRepository;
    }

    public void placeOrder(Order order) {
        InventoryReserveRequest req = new InventoryReserveRequest(order.getProduct(), order.getQuantity());
        Boolean reserved = inventoryClient.reserveInventory(req);
        if (reserved == null || !reserved) {
            throw new IllegalStateException("Insufficient inventory for product: " + order.getProduct());
        }
        orderRepository.save(order);
    }
}
