package com.farmar_mart.agriculture_order.service;
import com.farmar_mart.agriculture_order.dto.request.AddressRequest;
import com.farmar_mart.agriculture_order.dto.request.OrderRequest;
import com.farmar_mart.agriculture_order.dto.response.AddressResponse;
import com.farmar_mart.agriculture_order.dto.response.OrderItemResponse;
import com.farmar_mart.agriculture_order.dto.response.OrderResponse;
import com.farmar_mart.agriculture_order.model.Address;
import com.farmar_mart.agriculture_order.model.Order;
import com.farmar_mart.agriculture_order.model.OrderItem;
import com.farmar_mart.agriculture_order.repository.AddressRepository;
import com.farmar_mart.agriculture_order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AddressRepository addressRepository;
    @Override
    public OrderResponse placeOrder(OrderRequest orderRequest) {
        AddressRequest addressRequest = orderRequest.getAddress();
        Address address = new Address();
        address.setStreet(addressRequest.getStreet());
        address.setCity(addressRequest.getCity());
        address.setState(addressRequest.getState());
        address.setPostalCode(addressRequest.getPostalCode());
        address.setCountry(addressRequest.getCountry());
        addressRepository.save(address);
        Order order = new Order();
        order.setUserId(orderRequest.getUserId());
        order.setStatus("PLACED");
        order.setTotalPrice(0.0);
        order.setAddress(address);
        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0.0;

        if (orderRequest.getOrderItems() != null) {
            for (OrderItem itemReq : orderRequest.getOrderItems()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setProductId(itemReq.getProductId());
                orderItem.setQuantity(itemReq.getQuantity());
                orderItem.setPrice(itemReq.getPrice());
                orderItem.setOrder(order);

                totalPrice += itemReq.getPrice() * itemReq.getQuantity();
                orderItems.add(orderItem);
            }
        }

        order.setItems(orderItems);
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);

        return mapToOrderResponse(order);
    }
    @Override
    public OrderResponse getOrderById(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }
        return mapToOrderResponse(optionalOrder.get());
    }
    @Override
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        List<OrderResponse> responses = new ArrayList<>();

        for (Order order : orders) {
            responses.add(mapToOrderResponse(order));
        }
        return responses;
    }
    @Override
    public List<OrderResponse> getOrdersByStatus(String status) {
        List<Order> orders = orderRepository.findByStatus(status);
        List<OrderResponse> responses = new ArrayList<>();

        for (Order order : orders) {
            responses.add(mapToOrderResponse(order));
        }
        return responses;
    }
    @Override
    public void cancelOrder(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }

        Order order = optionalOrder.get();
        order.setStatus("CANCELLED");
        orderRepository.save(order);
    }
    private OrderResponse mapToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();

        response.setOrderId(order.getId());
        response.setUserId(order.getUserId());
        response.setStatus(order.getStatus());
        response.setTotalPrice(order.getTotalPrice());
        if (order.getAddress() != null) {
            AddressResponse addressResponse = new AddressResponse();
            addressResponse.setStreet(order.getAddress().getStreet());
            addressResponse.setCity(order.getAddress().getCity());
            addressResponse.setState(order.getAddress().getState());
            addressResponse.setPostalCode(order.getAddress().getPostalCode());
            addressResponse.setCountry(order.getAddress().getCountry());
            response.setAddress(addressResponse);
        }
        List<OrderItemResponse> itemResponses = new ArrayList<>();
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                OrderItemResponse itemResponse = new OrderItemResponse();
                itemResponse.setProductId(Long.valueOf(item.getProductId()));
                itemResponse.setQuantity((long) item.getQuantity());
                itemResponse.setPrice(item.getPrice());
                itemResponses.add(itemResponse);
            }
        }
        response.setItems(itemResponses);

        return response;
    }
}
