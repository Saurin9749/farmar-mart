package com.farmermart.orderservice.controller;

import com.farmermart.orderservice.dto.requestDto.CartItemRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.farmermart.orderservice.dto.requestDto.CartBulkRequest;
import com.farmermart.orderservice.dto.requestDto.CartRequest;
import com.farmermart.orderservice.dto.responseDto.CartResponse;
import com.farmermart.orderservice.service.CartService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ✅ Unit test for CartController using MockMvc and Mockito.
 */

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;


    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    // Helper methods for test data
    private CartRequest sampleCartRequest() {
        return new CartRequest(1L, 10L, 50.0, "Apple", 2);
    }

    private CartResponse sampleCartResponse() {
        return CartResponse.builder()
                .cartId(100L)
                .userId(1L)
                .productId(10L)
                .quantity(2)
                .totalPrice(100.0)
                .build();
    }

    @Test
    @DisplayName("POST /api/cart/add - should return CartResponse")
    void addToCart_returnsCartResponse() throws Exception {
        CartRequest req = sampleCartRequest();
        CartResponse resp = sampleCartResponse();

        when(cartService.addToCart(any(CartRequest.class))).thenReturn(resp);

        mockMvc.perform(post("/api/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cartId").value(100))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.productId").value(10))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.totalPrice").value(100.0));

        verify(cartService, times(1)).addToCart(any(CartRequest.class));
    }

    @Test
    @DisplayName("POST /api/cart/add/multiple - should return list of CartResponse")
    void addMultipleItems_returnsList() throws Exception {
        CartItemRequest item1 = new CartItemRequest(11L,  "Banana",10.0, 1);
        CartItemRequest item2 = new CartItemRequest(12L, "Orange",20.0, 2);

        CartBulkRequest bulkReq = new CartBulkRequest(1L, Arrays.asList(item1, item2));


        CartResponse r1 = CartResponse.builder()
                .cartId(101L).userId(1L).productId(11L).quantity(1).totalPrice(10.0).build();
        CartResponse r2 = CartResponse.builder()
                .cartId(102L).userId(1L).productId(12L).quantity(2).totalPrice(40.0).build();

        when(cartService.addMultipleItems(any(CartBulkRequest.class))).thenReturn(Arrays.asList(r1, r2));

        mockMvc.perform(post("/api/cart/add/multiple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bulkReq)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].cartId").value(101))
                .andExpect(jsonPath("$[1].cartId").value(102));

        verify(cartService, times(1)).addMultipleItems(any(CartBulkRequest.class));
    }

    @Test
    @DisplayName("POST /api/cart/remove - when service returns CartResponse")
    void removeFromCart_returnsCartResponse() throws Exception {
        CartRequest req = sampleCartRequest();
        CartResponse resp = sampleCartResponse();

        when(cartService.removeFromCart(any(CartRequest.class))).thenReturn(resp);

        mockMvc.perform(post("/api/cart/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value(100));

        verify(cartService, times(1)).removeFromCart(any(CartRequest.class));
    }

    @Test
    @DisplayName("POST /api/cart/remove - when service returns null (product removed)")
    void removeFromCart_returnsStringWhenNull() throws Exception {
        CartRequest req = sampleCartRequest();

        when(cartService.removeFromCart(any(CartRequest.class))).thenReturn(null);

        mockMvc.perform(post("/api/cart/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().string("Product removed from cart"));

        verify(cartService, times(1)).removeFromCart(any(CartRequest.class));
    }

    @Test
    @DisplayName("GET /api/cart/{userId} - should return list of CartResponse")
    void getUserCart_returnsList() throws Exception {
        long userId = 1L;
        CartResponse r1 = sampleCartResponse();
        List<CartResponse> list = Arrays.asList(r1);

        when(cartService.getUserCart(userId)).thenReturn(list);

        mockMvc.perform(get("/api/cart/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].userId").value(1));

        verify(cartService, times(1)).getUserCart(userId);
    }

    @Test
    @DisplayName("DELETE /api/cart/{userId} - should clear cart and return message")
    void clearCart_returnsSuccessMessage() throws Exception {
        long userId = 1L;
        doNothing().when(cartService).clearCart(userId);

        mockMvc.perform(delete("/api/cart/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().string("Cart cleared successfully"));

        verify(cartService, times(1)).clearCart(userId);
    }
}
