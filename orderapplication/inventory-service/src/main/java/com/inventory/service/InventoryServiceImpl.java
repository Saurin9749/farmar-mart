package com.inventory.service;

import com.inventory.repository.InventoryRepository;
import com.inventory.model.Inventory;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository repository;

    public InventoryServiceImpl(InventoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void updateInventory(String product, int quantity) {
        Inventory item = repository.findByProduct(product);
        if (item == null) {
            System.out.println("InventoryService: product not found -> " + product);
            return;
        }
        int available = item.getQuantity();
        if (available >= quantity) {
            repository.updateStock(product, available - quantity);
            System.out.println("InventoryService: updated " + product + " -> remaining " + (available - quantity));
        } else {
            System.out.println("InventoryService: insufficient stock for " + product + " (available=" + available + ", requested=" + quantity + ")");
        }
    }
}
