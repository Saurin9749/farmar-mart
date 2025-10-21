package com.farmermart.orderservice.controller;

import com.farmermart.orderservice.model.PurchasedProduct;
import com.farmermart.orderservice.service.PurchasedProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/purchased")
@RequiredArgsConstructor
public class PurchasedProductController {
    private final PurchasedProductService purchasedProductService;
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PurchasedProduct>> getUserPurchasedProducts(@PathVariable Long userId) {
        List<PurchasedProduct> products = purchasedProductService.getPurchasedProductsByUser(userId);
        return ResponseEntity.ok(products);
    }
    @GetMapping("/all")
    public ResponseEntity<List<PurchasedProduct>> getAllPurchasedProducts() {
        List<PurchasedProduct> allProducts = purchasedProductService.getAllPurchasedProducts();
        return ResponseEntity.ok(allProducts);
    }
}
