package com.inventory.repository;

import com.inventory.model.Inventory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class InventoryRepositoryImpl implements InventoryRepository {

    private final Map<String, Inventory> inventoryMap = new HashMap<>();

    public InventoryRepositoryImpl() {
        inventoryMap.put("Laptop", new Inventory("Laptop", 10));
        inventoryMap.put("Phone", new Inventory("Phone", 20));
        inventoryMap.put("Tablet", new Inventory("Tablet", 5));
    }

    @Override
    public Inventory findByProduct(String product) {
        return inventoryMap.get(product);
    }

    @Override
    public void updateStock(String product, int newQuantity) {
        inventoryMap.put(product, new Inventory(product, newQuantity));
    }

    @Override
    public Map<String, Inventory> findAll() {
        return new HashMap<>(inventoryMap);
    }
}
