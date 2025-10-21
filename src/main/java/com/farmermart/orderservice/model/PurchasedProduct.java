package com.farmermart.orderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "purchased_products",uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id","product_id"})} )
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchasedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id",nullable = false)
    private Long userId;

    @Column(name ="product_id",nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long purchasedCount = 0L;

    public PurchasedProduct(Long userId, Long productId, Long purchasedCount) {
        this.userId = userId;
        this.purchasedCount = purchasedCount;
        this.productId = productId;
    }
}
