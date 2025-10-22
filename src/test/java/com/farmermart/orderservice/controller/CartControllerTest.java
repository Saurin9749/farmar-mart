package com.farmermart.orderservice.controller;

import com.farmermart.orderservice.dto.requestDto.CartItemRequest;
import com.farmermart.orderservice.dto.requestDto.CartBulkRequest;
import com.farmermart.orderservice.dto.requestDto.CartRequest;
import com.farmermart.orderservice.dto.responseDto.CartResponse;
import com.farmermart.orderservice.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ✅ Integration test for CartController using MockMvc with real Spring context.
 */
@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CartService cartService; // real service

    @Autowired
    private ObjectMapper objectMapper;

    // Helper methods
    private CartRequest sampleCartRequest() {
        return new CartRequest(1L, 10L, 50.0, "Apple", 2);
    }

    private CartItemRequest sampleCartItem1() {
        return new CartItemRequest(11L, "Banana", 10.0, 1);
    }

    private CartItemRequest sampleCartItem2() {
        return new CartItemRequest(12L, "Orange", 20.0, 2);
    }

    @Test
    @DisplayName("POST /api/cart/add - should add item to cart")
    void addToCart() throws Exception {
        CartRequest req = sampleCartRequest();

        mockMvc.perform(post("/api/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        // The response will come from the real service
    }

    @Test
    @DisplayName("POST /api/cart/add/multiple - should add multiple items")
    void addMultipleItems() throws Exception {
        CartBulkRequest bulkReq = new CartBulkRequest(1L, Arrays.asList(sampleCartItem1(), sampleCartItem2()));

        mockMvc.perform(post("/api/cart/add/multiple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bulkReq)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("POST /api/cart/remove - should remove item from cart")
    void removeFromCart() throws Exception {
        CartRequest req = sampleCartRequest();

        mockMvc.perform(post("/api/cart/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/cart/{userId} - should get user cart")
    void getUserCart() throws Exception {
        long userId = 1L;

        mockMvc.perform(get("/api/cart/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("DELETE /api/cart/{userId} - should clear user cart")
    void clearCart() throws Exception {
        long userId = 1L;

        mockMvc.perform(delete("/api/cart/{userId}", userId))
                .andExpect(status().isOk());
    }
}
