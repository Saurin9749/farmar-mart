package com.farmermart.orderservice.service;
import com.farmermart.orderservice.dto.requestDto.CartRequest;
import com.farmermart.orderservice.dto.responseDto.CartResponse;
import com.farmermart.orderservice.model.Cart;
import com.farmermart.orderservice.repository.CartRepository;
import com.farmermart.orderservice.service.Impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private Cart cart;
    private CartRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new CartRequest(1L, 2L, 100.0); // userId=1, productId=2, price=100
        cart = new Cart(1L, 2L, 1, 100.0);
    }

    @Test
    void testAddToCart_NewItem_ShouldCreateNewCartEntry() {
        when(cartRepository.findByUserIdAndProductId(1L, 2L)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenAnswer(inv -> inv.getArgument(0));

        CartResponse response = cartService.addToCart(request);

        assertEquals(1L, response.getUserId());
        assertEquals(2L, response.getProductId());
        assertEquals(1, response.getQuantity());
        assertEquals(100.0, response.getTotalPrice());
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void testAddToCart_ExistingItem_ShouldIncreaseQuantity() {
        cart.setQuantity(2);
        when(cartRepository.findByUserIdAndProductId(1L, 2L)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(inv -> inv.getArgument(0));

        CartResponse response = cartService.addToCart(request);

        assertEquals(3, response.getQuantity());
        assertEquals(300.0, response.getTotalPrice());
    }

    @Test
    void testRemoveFromCart_WhenQuantityBecomesZero_ShouldDelete() {
        cart.setQuantity(1);
        when(cartRepository.findByUserIdAndProductId(1L, 2L)).thenReturn(Optional.of(cart));

        CartResponse response = cartService.removeFromCart(request);

        assertEquals(0, response.getQuantity());
        verify(cartRepository).delete(cart);
    }

    @Test
    void testGetUserCart_ShouldReturnMappedResponses() {
        when(cartRepository.findByUserId(1L)).thenReturn(List.of(cart));

        List<CartResponse> result = cartService.getUserCart(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUserId());
    }
}

