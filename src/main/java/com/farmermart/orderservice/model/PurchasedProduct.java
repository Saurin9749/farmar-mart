package com.farmermart.orderservice.model;

import com.farmermart.orderservice.audit.BaseAuditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "purchased_products",uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id","product_id"})} )
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class PurchasedProduct extends BaseAuditable {

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
