package com.farmermart.orderservice.service.Impl;

import com.farmermart.orderservice.model.PurchasedProduct;
import com.farmermart.orderservice.repository.PurchasedProductRepository;
import com.farmermart.orderservice.service.PurchasedProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchasedProductServiceImpl implements PurchasedProductService {

    private final PurchasedProductRepository purchasedProductRepository;

    @Override
    public List<PurchasedProduct> getPurchasedProductsByUser(Long userId) {
        return purchasedProductRepository.findByUserIdOrderByPurchasedCountDesc(userId);
    }

    @Override
    public List<PurchasedProduct> getAllPurchasedProducts() {
        return purchasedProductRepository.findAll();
    }
}
