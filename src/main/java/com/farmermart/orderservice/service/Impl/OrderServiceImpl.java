package com.farmermart.orderservice.service.Impl;

import com.farmermart.orderservice.dto.requestDto.AddressRequest;
import com.farmermart.orderservice.dto.requestDto.OrderItemRequest;
import com.farmermart.orderservice.dto.requestDto.OrderRequest;
import com.farmermart.orderservice.dto.responseDto.AddressResponse;
import com.farmermart.orderservice.dto.responseDto.OrderItemResponse;
import com.farmermart.orderservice.dto.responseDto.OrderResponse;
import com.farmermart.orderservice.dto.responseDto.ProductResponse;
import com.farmermart.orderservice.exception.InvalidRequestException;
import com.farmermart.orderservice.exception.ResourceNotFoundException;
import com.farmermart.orderservice.feign.CartClient;
import com.farmermart.orderservice.feign.ProductClient;
import com.farmermart.orderservice.feign.UserClient;
import com.farmermart.orderservice.model.Address;
import com.farmermart.orderservice.model.Order;
import com.farmermart.orderservice.model.OrderItem;
import com.farmermart.orderservice.model.PurchasedProduct;
import com.farmermart.orderservice.repository.AddressRepository;
import com.farmermart.orderservice.repository.OrderRepository;
import com.farmermart.orderservice.repository.PurchasedProductRepository;
import com.farmermart.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserClient userClient;
    private final ProductClient productClient;
    private final ModelMapper modelMapper;
    private final AddressRepository addressRepository;
    private final CartClient cartClient;
    private final PurchasedProductRepository purchasedProductRepository;





    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        if (request == null)
            throw new InvalidRequestException("Order request cannot be null");

        if (request.getUserId() == null)
            throw new InvalidRequestException("User ID is required");

        if (!userClient.validateUser(request.getUserId())) {
            throw new InvalidRequestException("Invalid user ID");
        }
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setStatus("PLACED");
        order.setCreatedAt(LocalDateTime.now());
        Address address = modelMapper.map(request.getAddress(), Address.class);
        order.setAddress(address);
        List<OrderItem> items = request.getItems().stream().map(itemReq -> {
            if (itemReq.getProductId() == null || itemReq.getQuantity() == null || itemReq.getQuantity() <= 0) {
                throw new InvalidRequestException("Invalid order item data");
            }
            ProductResponse product = productClient.getProduct(itemReq.getProductId());
            if (product == null)
                throw new ResourceNotFoundException("Product not found: " + itemReq.getProductId());
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getProductId());
            orderItem.setProductName(product.getName());
            orderItem.setQuantity(itemReq.getQuantity());
            orderItem.setTotalPrice(product.getPrice() * itemReq.getQuantity());
            orderItem.setOrder(order);
            return orderItem;
        }).collect(Collectors.toList());

        order.setItems(items);
        double totalOrderPrice = items.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
        order.setTotalPrice(totalOrderPrice);
        Order savedOrder = orderRepository.save(order);
        for (OrderItemRequest itemReq : request.getItems()) {
            Long productId = itemReq.getProductId();
            int quantity = itemReq.getQuantity();
            if (Boolean.TRUE.equals(itemReq.getFromCart())) {
                try {
                    cartClient.removeItemFromCart(request.getUserId(), productId);
                } catch (Exception e) {
                    System.err.println("Failed to remove product " + productId + " from cart: " + e.getMessage());
                }
            }
            PurchasedProduct purchased = purchasedProductRepository
                    .findByUserIdAndProductId(request.getUserId(), productId)
                    .orElseGet(() -> new PurchasedProduct(request.getUserId(), productId, 0L));

            purchased.setPurchasedCount(purchased.getPurchasedCount() + quantity);
            purchasedProductRepository.save(purchased);
        }

        return mapToOrderResponse(savedOrder);
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return mapToOrderResponse(order);
    }

    @Override
    public List<OrderResponse> getOrdersByUser(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found for user: " + userId);
        }
        return orders.stream().map(this::mapToOrderResponse).collect(Collectors.toList());
    }

    @Override
    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        order.setStatus("CANCELLED");
        orderRepository.save(order);
    }
    private OrderResponse mapToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getId());
        response.setUserId(order.getUserId());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt());
        response.setTotalPrice(order.getTotalPrice());
        response.setAddress(modelMapper.map(order.getAddress(), AddressResponse.class));
        List<OrderItemResponse> itemResponses = order.getItems().stream().map(item -> {
            OrderItemResponse r = new OrderItemResponse();
            r.setProductId(item.getProductId());
            r.setProductName(item.getProductName());
            r.setQuantity(item.getQuantity());
            r.setPrice(item.getTotalPrice());
            return r;
        }).collect(Collectors.toList());

        response.setItems(itemResponses);
        return response;
    }


    @Override
    public void editOrderAddress(Long id, AddressRequest newAddress) {

        Order order = orderRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Order not found with id : " + id));

        Address address = order.getAddress();
        address.setStreet(newAddress.getStreet());
        address.setCity(newAddress.getCity());
        address.setState(newAddress.getState());
        address.setZipCode(newAddress.getZipCode());
        address.setCountry(newAddress.getCountry());

        addressRepository.save(address);

    }

    @Override
    public String makePayment(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Order not found with id : " + id));

        if("PAID".equalsIgnoreCase(order.getStatus())){
            return "Payment being done already for this particular order";
        }

        boolean paySuccess = true;

        if(paySuccess) {
            order.setStatus("PAID");
            orderRepository.save(order);
            return "Payment successful for Order Id: " + id;
        } else {
            order.setStatus("Payment_Failed");
            orderRepository.save(order);
            return "Payment failed for Order Id: " + id + "Please try again";
        }

    }

}
