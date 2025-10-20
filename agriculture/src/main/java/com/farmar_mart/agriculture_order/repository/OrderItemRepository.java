package com.farmar_mart.agriculture_order.repository;

import com.farmar_mart.agriculture_order.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(Long orderId);

    List<OrderItem> findByUserId(Long userId);
}
