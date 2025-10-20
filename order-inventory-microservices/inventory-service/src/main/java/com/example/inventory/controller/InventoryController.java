package com.example.inventory.controller;

import com.example.inventory.model.Inventory;
import com.example.inventory.repository.InventoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryRepository repository;

    public InventoryController(InventoryRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Inventory> all() {
        return repository.findAll();
    }

    @PostMapping
    public Inventory add(@RequestBody Inventory inventory) {
        return repository.save(inventory);
    }

    @PostMapping("/reserve")
    public ResponseEntity<Boolean> reserve(@RequestBody Inventory request) {
        Optional<Inventory> found = repository.findById(request.getProduct());
        if (found.isEmpty() || found.get().getQuantity() < request.getQuantity()) {
            return ResponseEntity.ok(false);
        }
        Inventory inv = found.get();
        inv.setQuantity(inv.getQuantity() - request.getQuantity());
        repository.save(inv);
        return ResponseEntity.ok(true);
    }
}
