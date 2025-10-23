package com.farmermart.orderservice.controller;

import com.farmermart.orderservice.dto.requestDto.CartBulkRequest;
import com.farmermart.orderservice.dto.requestDto.CartItemRequest;
import com.farmermart.orderservice.dto.requestDto.CartRequest;
import com.farmermart.orderservice.dto.responseDto.CartResponse;
import com.farmermart.orderservice.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * ✅ Unit tests for CartController using JUnit 5 + Mockito
 * Updated to match CartBulkRequest with userId + List<CartItemRequest>
 */
class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ✅ Helper: Sample CartRequest
    private CartRequest sampleCartRequest() {
        return new CartRequest(1L, 10L, 50.0, "Apple", 2);
    }

    // ✅ Helper: Sample CartItemRequest
    private CartItemRequest sampleCartItemRequest(Long productId, String name, Double price, int qty) {
        return new CartItemRequest(productId, name, price, qty);
    }

    // ✅ Test: addToCart()
    @Test
    @DisplayName("addToCart() should return added cart item details")
    void testAddToCart() {
        CartRequest request = sampleCartRequest();
        CartResponse mockResponse = new CartResponse(1L, 1L, 10L, 2, 100.0);

        when(cartService.addToCart(request)).thenReturn(mockResponse);

        ResponseEntity<CartResponse> response = cartController.addToCart(request);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getUserId());
        assertEquals(100.0, response.getBody().getTotalPrice());

        verify(cartService, times(1)).addToCart(request);
    }

    // ✅ Test: addMultipleItems() with CartBulkRequest (using CartItemRequest)
    @Test
    @DisplayName("addMultipleItems() should add multiple items and return list of CartResponse")
    void testAddMultipleItems() {
        CartBulkRequest bulkRequest = new CartBulkRequest(
                1L,
                List.of(
                        sampleCartItemRequest(10L, "Apple", 50.0, 2),
                        sampleCartItemRequest(11L, "Banana", 30.0, 3)
                )
        );

        List<CartResponse> mockResponses = List.of(
                new CartResponse(1L, 1L, 10L, 2, 100.0),
                new CartResponse(2L, 1L, 11L, 3, 90.0)
        );

        when(cartService.addMultipleItems(bulkRequest)).thenReturn(mockResponses);

        ResponseEntity<List<CartResponse>> response = cartController.addMultipleItems(bulkRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(90.0, response.getBody().get(1).getTotalPrice());

        verify(cartService, times(1)).addMultipleItems(bulkRequest);
    }

    // ✅ Test: removeFromCart() returns updated item (not null)
    @Test
    @DisplayName("removeFromCart() should return updated cart item if still present")
    void testRemoveFromCart_ReturnsItem() {
        CartRequest request = sampleCartRequest();
        CartResponse mockResponse = new CartResponse(1L, 1L, 10L, 1, 50.0);

        when(cartService.removeFromCart(request)).thenReturn(mockResponse);

        ResponseEntity<?> response = cartController.removeFromCart(request);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof CartResponse);
        CartResponse body = (CartResponse) response.getBody();
        assertEquals(1, body.getQuantity());

        verify(cartService, times(1)).removeFromCart(request);
    }

    // ✅ Test: removeFromCart() returns message if product removed
    @Test
    @DisplayName("removeFromCart() should return message when product removed completely")
    void testRemoveFromCart_ReturnsMessage() {
        CartRequest request = sampleCartRequest();

        when(cartService.removeFromCart(request)).thenReturn(null);

        ResponseEntity<?> response = cartController.removeFromCart(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Product removed from cart", response.getBody());

        verify(cartService, times(1)).removeFromCart(request);
    }

    // ✅ Test: getUserCart()
    @Test
    @DisplayName("getUserCart() should return list of cart items for user")
    void testGetUserCart() {
        long userId = 1L;
        List<CartResponse> mockCart = List.of(
                new CartResponse(1L, userId, 10L, 2, 100.0)
        );

        when(cartService.getUserCart(userId)).thenReturn(mockCart);

        ResponseEntity<List<CartResponse>> response = cartController.getUserCart(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(100.0, response.getBody().get(0).getTotalPrice());

        verify(cartService, times(1)).getUserCart(userId);
    }

    // ✅ Test: clearCart()
    @Test
    @DisplayName("clearCart() should call service and return success message")
    void testClearCart() {
        long userId = 1L;
        doNothing().when(cartService).clearCart(userId);

        ResponseEntity<String> response = cartController.clearCart(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Cart cleared successfully", response.getBody());

        verify(cartService, times(1)).clearCart(userId);
    }
}
