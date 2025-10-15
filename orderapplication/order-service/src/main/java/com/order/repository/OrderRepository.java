package com.order.repository;

import com.order.model.Order;
import java.util.List;

public interface OrderRepository {
    void save(Order order);
    List<Order> findAll();
}
