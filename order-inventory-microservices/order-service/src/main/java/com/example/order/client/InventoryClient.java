package com.example.order.client;

import com.example.order.dto.InventoryReserveRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @PostMapping("/inventory/reserve")
    Boolean reserveInventory(InventoryReserveRequest request);
}
