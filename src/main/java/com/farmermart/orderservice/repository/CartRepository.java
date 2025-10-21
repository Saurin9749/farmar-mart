package com.farmermart.orderservice.repository;
import com.farmermart.orderservice.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserIdAndProductId(Long userId, Long productId);

    void deleteByUserIdAndProductId(Long userId, Long productId);

    List<Cart> findByUserId(Long userId);

    void deleteAllByUserId(Long userId);
}
