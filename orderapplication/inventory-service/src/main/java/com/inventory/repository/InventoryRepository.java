package com.inventory.repository;

import com.inventory.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    Inventory findByProduct(String product);
    void updateStock(String product, int newQuantity);
    Map<String, Inventory> findAll();
}
