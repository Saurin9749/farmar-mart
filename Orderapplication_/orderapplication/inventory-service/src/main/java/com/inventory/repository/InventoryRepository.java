package com.inventory.repository;

import com.inventory.model.Inventory;
import java.util.Map;

public interface InventoryRepository {
    Inventory findByProduct(String product);
    void updateStock(String product, int newQuantity);
    Map<String, Inventory> findAll();
}
