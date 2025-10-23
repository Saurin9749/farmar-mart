
package com.farmermart.orderservice.controller;
import com.farmermart.orderservice.dto.requestDto.CartRequest;
import com.farmermart.orderservice.dto.responseDto.CartResponse;
import com.farmermart.orderservice.service.Impl.CartServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
        import java.util.List;
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartServiceImpl cartService;

    public CartController(CartServiceImpl cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(@RequestBody CartRequest request) {
        CartResponse response = cartService.addToCart(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeFromCart(@RequestBody CartRequest request) {
        CartResponse response = cartService.removeFromCart(request);

        if (response == null) {
            return ResponseEntity.ok("Product removed from cart");
        }

        return ResponseEntity.ok(response);
    }
    @GetMapping("/getUserCart/{userId}")
    public ResponseEntity<List<CartResponse>> getUserCart(@PathVariable Long userId) {
        List<CartResponse> userCart = cartService.getUserCart(userId);
        return ResponseEntity.ok(userCart);
    }

    @DeleteMapping("clearCart/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok("Cart cleared successfully");
    }
}
