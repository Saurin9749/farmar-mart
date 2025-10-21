package com.farmermart.orderservice.service;

import com.farmermart.orderservice.model.PurchasedProduct;

import java.util.List;

public interface PurchasedProductService {
    List<PurchasedProduct> getPurchasedProductsByUser(Long userId);
    List<PurchasedProduct> getAllPurchasedProducts();
}
