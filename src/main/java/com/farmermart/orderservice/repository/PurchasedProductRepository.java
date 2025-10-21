package com.farmermart.orderservice.repository;
import com.farmermart.orderservice.model.PurchasedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchasedProductRepository extends JpaRepository<PurchasedProduct, Long> {
    Optional<PurchasedProduct> findByUserIdAndProductId(Long userId, Long productId);
    List<PurchasedProduct> findByUserIdOrderByPurchasedCountDesc(Long userId);
}
