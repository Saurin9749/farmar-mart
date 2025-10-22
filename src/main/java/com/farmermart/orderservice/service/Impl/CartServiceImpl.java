package com.farmermart.orderservice.service.Impl;

import com.farmermart.orderservice.dto.requestDto.CartBulkRequest;
import com.farmermart.orderservice.dto.requestDto.CartRequest;
import com.farmermart.orderservice.dto.requestDto.CartItemRequest;
import com.farmermart.orderservice.dto.responseDto.CartResponse;
import com.farmermart.orderservice.model.Cart;
import com.farmermart.orderservice.repository.CartRepository;
import com.farmermart.orderservice.service.CartService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    @Transactional
    public CartResponse addToCart(CartRequest request) {
        Cart cart = cartRepository.findByUserIdAndProductId(request.getUserId(), request.getProductId())
                .orElseGet(() -> new Cart(request.getUserId(), request.getProductId(), 0, 0.0));

        cart.setQuantity(cart.getQuantity() + request.getQuantity());
        cart.setTotalPrice(cart.getQuantity() * request.getProductPrice());
        cartRepository.save(cart);

        return toResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse removeFromCart(CartRequest request) {
        Cart cart = cartRepository.findByUserIdAndProductId(request.getUserId(), request.getProductId()).orElse(null);
        if (cart == null) return null;

        cart.setQuantity(cart.getQuantity() - 1);
        if (cart.getQuantity() <= 0) {
            cartRepository.delete(cart);
            return new CartResponse(cart.getId(), cart.getUserId(), cart.getProductId(), 0, 0.0);
        }

        cart.setTotalPrice(cart.getQuantity() * request.getProductPrice());
        cartRepository.save(cart);
        return toResponse(cart);
    }

    @Override
    public List<CartResponse> getUserCart(Long userId) {
        return cartRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        cartRepository.deleteAllByUserId(userId);
    }

    @Override
    public List<CartResponse> addMultipleItems(CartBulkRequest request) {
        return request.getItems().stream()
                .map((CartItemRequest item) -> {
                    CartRequest singleRequest = new CartRequest(
                            request.getUserId(),
                            item.getProductId(),
                            item.getPrice(),
                            item.getProductName(),
                            item.getQuantity()
                    );
                    return addToCart(singleRequest);
                })
                .collect(Collectors.toList());
    }

    private CartResponse toResponse(Cart cart) {
        return new CartResponse(cart.getId(), cart.getUserId(), cart.getProductId(), cart.getQuantity(), cart.getTotalPrice());
    }
}
