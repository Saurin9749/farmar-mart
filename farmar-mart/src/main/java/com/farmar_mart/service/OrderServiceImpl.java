package com.farmar_mart.service;
import com.farmar_mart.dto.request.AddressRequest;
import com.farmar_mart.dto.request.OrderRequest;
import com.farmar_mart.dto.response.*;
import com.farmar_mart.feign.ProductClient;
import com.farmar_mart.feign.UserClient;
import com.farmar_mart.model.Address;
import com.farmar_mart.model.Order;
import com.farmar_mart.model.OrderItem;
import com.farmar_mart.repository.AddressRepository;
import com.farmar_mart.repository.OrderItemRepository;
import com.farmar_mart.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final AddressRepository addressRepository;
    private final UserClient userClient;
    private final ProductClient productClient;

    @Override
    public OrderResponse placeOrder(OrderRequest orderRequest) {
        UserResponse user = userClient.getUserById(orderRequest.getUserId());
        if (user == null) {
            throw new RuntimeException("User not found for ID: " + orderRequest.getUserId());
        }
        Address address = mapAddressRequestToEntity(orderRequest.getAddress());
        addressRepository.save(address);
        Order order = new Order();
        order.setUserId(orderRequest.getUserId());
        order.setStatus("PLACED");
        order.setAddress(address);
        List<OrderItem> orderItems = orderRequest.getOrderItems().stream().map(item -> {
            ProductResponse product = productClient.getProductById(Long.valueOf(item.getProductId()));
            if (product == null || product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Product not available or insufficient stock for ID: " + item.getProductId());
            }
            productClient.updateStock(product.getId(), product.getStock() - item.getQuantity());

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(item.getProductId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setOrder(order);
            return orderItem;
        }).collect(Collectors.toList());

        order.setItems(orderItems);
        double totalPrice = orderItems.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);
        return mapToOrderResponse(savedOrder);
    }

    @Override
    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        OrderResponse response = mapToOrderResponse(order);
        response.setUserId(userClient.getUserById(order.getUserId()).getId());
        return response;
    }

    @Override
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(order -> {
                    OrderResponse response = mapToOrderResponse(order);
                    response.setUserId(userClient.getUserById(userId).getId());
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status)
                .stream()
                .map(order -> {
                    OrderResponse response = mapToOrderResponse(order);
                    response.setUserId(userClient.getUserById(order.getUserId()).getId());
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        if (!order.getStatus().equalsIgnoreCase("CANCELLED")) {
            order.setStatus("CANCELLED");
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Order already cancelled!");
        }
    }
    private Address mapAddressRequestToEntity(AddressRequest addressRequest) {
        Address address = new Address();
        address.setStreet(addressRequest.getStreet());
        address.setCity(addressRequest.getCity());
        address.setState(addressRequest.getState());
        address.setPostalCode(addressRequest.getPostalCode());
        address.setCountry(addressRequest.getCountry());
        return address;
    }

    private OrderResponse mapToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getId());
        response.setUserId(order.getUserId());
        response.setStatus(order.getStatus());
        response.setTotalPrice(order.getTotalPrice());

        AddressResponse addressResponse = new AddressResponse();
        if (order.getAddress() != null) {
            addressResponse.setStreet(order.getAddress().getStreet());
            addressResponse.setCity(order.getAddress().getCity());
            addressResponse.setState(order.getAddress().getState());
            addressResponse.setPostalCode(order.getAddress().getPostalCode());
            addressResponse.setCountry(order.getAddress().getCountry());
        }
        response.setAddress(addressResponse);

        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(item -> {
                    OrderItemResponse itemResponse = new OrderItemResponse();
                    itemResponse.setProductId(Long.valueOf(item.getProductId()));
                    itemResponse.setQuantity((long) item.getQuantity());
                    itemResponse.setPrice(item.getPrice());
                    return itemResponse;
                }).collect(Collectors.toList());
        response.setItems(itemResponses);
        return response;
    }

}
