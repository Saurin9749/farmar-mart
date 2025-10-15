package com.order.repository;

import com.order.model.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final List<Order> orders = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void save(Order order) {
        orders.add(order);
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(orders);
    }
}
