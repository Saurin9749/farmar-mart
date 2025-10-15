package com.inventory.service.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.service.InventoryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    private final ObjectMapper mapper = new ObjectMapper();
    private final InventoryService inventoryService;

    public OrderConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(topics = "order-topic", groupId = "inventory-group")
    public void consume(String message) {
        try {
            System.out.println("OrderConsumer received -> " + message);
            JsonNode node = mapper.readTree(message);
            String product = node.get("product").asText();
            int quantity = node.has("quantity") ? node.get("quantity").asInt() : 1;
            inventoryService.updateInventory(product, quantity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
