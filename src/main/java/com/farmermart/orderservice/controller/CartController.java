package com.farmermart.orderservice.controller;
import com.farmermart.orderservice.dto.requestDto.CartBulkRequest;
import com.farmermart.orderservice.dto.requestDto.CartRequest;
import com.farmermart.orderservice.dto.responseDto.CartResponse;
import com.farmermart.orderservice.service.CartService;
import com.farmermart.orderservice.service.Impl.CartServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(@RequestBody CartRequest request) {
        CartResponse response = cartService.addToCart(request);
        return ResponseEntity.ok(response);
    }

    //pooja
    @PostMapping("/add/multiple")
    public ResponseEntity<List<CartResponse>> addMultipleItems(@RequestBody CartBulkRequest request) {
        List<CartResponse> responses = cartService.addMultipleItems(request);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeFromCart(@RequestBody CartRequest request) {
        CartResponse response = cartService.removeFromCart(request);

        if (response == null) {
            return ResponseEntity.ok("Product removed from cart");
        }

        return ResponseEntity.ok(response);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartResponse>> getUserCart(@PathVariable Long userId) {
        List<CartResponse> userCart = cartService.getUserCart(userId);
        return ResponseEntity.ok(userCart);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok("Cart cleared successfully");
    }
}