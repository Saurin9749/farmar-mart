package com.farmermart.orderservice.service.Impl;
import com.farmermart.orderservice.dto.requestDto.CartRequest;
import com.farmermart.orderservice.dto.responseDto.CartResponse;
import com.farmermart.orderservice.model.Cart;
import com.farmermart.orderservice.repository.CartRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl {

    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Transactional
    public CartResponse addToCart(CartRequest request) {
        Cart cart = cartRepository.findByUserIdAndProductId(request.getUserId(), request.getProductId()).orElseGet(() -> new Cart(request.getUserId(), request.getProductId(), 0, 0.0));
        cart.setQuantity(cart.getQuantity() + 1);
        cart.setTotalPrice(cart.getQuantity() * request.getProductPrice());
        cartRepository.save(cart);
        return toResponse(cart);
    }

    @Transactional
    public CartResponse removeFromCart(CartRequest request) {
        Cart cart = cartRepository.findByUserIdAndProductId(request.getUserId(), request.getProductId()).orElse(null);
        if (cart == null) return null;
        cart.setQuantity(cart.getQuantity() - 1);
        if (cart.getQuantity() <= 0) {
            cartRepository.delete(cart);
            return CartResponse.builder().cartId(cart.getId()).userId(cart.getUserId()).productId(cart.getProductId()).quantity(0).totalPrice(0.0).build();
        }
        cart.setTotalPrice(cart.getQuantity() * request.getProductPrice());
        cartRepository.save(cart);

        return toResponse(cart);
    }

    public List<CartResponse> getUserCart(Long userId) {
        return cartRepository.findByUserId(userId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public void clearCart(Long userId) {
        cartRepository.deleteAllByUserId(userId);
    }
    private CartResponse toResponse(Cart cart) {
        return new CartResponse(cart.getId(), cart.getUserId(), cart.getProductId(), cart.getQuantity(), cart.getTotalPrice());
    }
}
