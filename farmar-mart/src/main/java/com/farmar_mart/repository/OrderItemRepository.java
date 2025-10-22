package com.farmar_mart.repository;
import com.farmar_mart.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder_UserId(Long orderId);

    List<OrderItem> findByOrder_Id(Long userId);
}
