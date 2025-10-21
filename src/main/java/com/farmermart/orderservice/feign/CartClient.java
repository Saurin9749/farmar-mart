package com.farmermart.orderservice.feign;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CartClient {
    private final String CART_SERVICE_BASE = "http://localhost:8081/api/cart";
    private final RestTemplate restTemplate = new RestTemplate();
    public void removeItemFromCart(Long userId, Long productId) {
        try {
            String url = CART_SERVICE_BASE + "/" + userId + "/items/" + productId;
            restTemplate.delete(url);
        } catch (Exception ex) {
            System.err.println("Failed to remove item from cart for user " + userId + " product " + productId + " : " + ex.getMessage());
        }
    }
}
