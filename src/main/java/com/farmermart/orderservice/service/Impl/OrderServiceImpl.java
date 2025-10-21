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
import com.farmermart.orderservice.feign.ProductClient;
import com.farmermart.orderservice.feign.UserClient;
import com.farmermart.orderservice.model.Address;
import com.farmermart.orderservice.model.Order;
import com.farmermart.orderservice.model.OrderItem;
import com.farmermart.orderservice.repository.AddressRepository;
import com.farmermart.orderservice.repository.OrderRepository;
import com.farmermart.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserClient userClient;
    private final ProductClient productClient;
    private final ModelMapper modelMapper;
    private final AddressRepository addressRepository;



    @Override
    public OrderResponse createOrder(OrderRequest request) {
       if(!userClient.validateUser(request.getUserId())){
           throw new InvalidRequestException("Invalid User Id");
       }
       if(request.getItems() == null || request.getItems().isEmpty()){
           throw new InvalidRequestException("Order must contain at least 1 product");
       }

       Order o = new Order();
       o.setUserId(request.getUserId());
       o.setAddress(modelMapper.map(request.getAddress(), Address.class));
       o.setStatus("CREATED");
       o.setCreatedAt(LocalDateTime.now());


        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0.0;

        for (OrderItemRequest itemReq : request.getItems()) {
            ProductResponse product = productClient.getProduct(itemReq.getProductId());
            if (product == null) {
                throw new InvalidRequestException("Invalid Product Id " + itemReq.getProductId());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getProductId());
            orderItem.setProductName(product.getName());
            orderItem.setQuantity(itemReq.getQuantity());
            orderItem.setTotalPrice(product.getPrice() * itemReq.getQuantity());
            orderItem.setOrder(o);

            totalPrice += orderItem.getTotalPrice();
            orderItems.add(orderItem);
        }

        o.setItems(orderItems);
        o.setTotalPrice(totalPrice);

        // save order in database
        Order savedOrder = orderRepository.save(o);

        // Map order entity to order response dto
        OrderResponse orderResponse = modelMapper.map(savedOrder,OrderResponse.class);

        List<OrderItemResponse> itemsResp = savedOrder.getItems().stream().map(item -> {
            OrderItemResponse i = new OrderItemResponse();
            i.setProductId(item.getProductId());
            i.setProductName(item.getProductName());
            i.setQuantity(item.getQuantity());
            i.setPrice(item.getTotalPrice());
            return i;
        }).toList();

        orderResponse.setItems(itemsResp);
        orderResponse.setAddress(modelMapper.map(savedOrder.getAddress(), AddressResponse.class));
        orderResponse.setCreatedAt(savedOrder.getCreatedAt());

        return orderResponse;

    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Order not found with id : " + id));
        OrderResponse orderResponse = modelMapper.map(order,OrderResponse.class);

        List<OrderItemResponse> itemsResp = order.getItems().stream().map(item -> {
            OrderItemResponse i = new OrderItemResponse();
            i.setProductId(item.getProductId());
            i.setProductName(item.getProductName());
            i.setQuantity(item.getQuantity());
            i.setPrice(item.getTotalPrice());
            return i;
        }).toList();

        orderResponse.setItems(itemsResp);
        orderResponse.setAddress(modelMapper.map(order.getAddress(), AddressResponse.class));
        orderResponse.setCreatedAt(order.getCreatedAt());

        return orderResponse;

    }

    @Override
    public List<OrderResponse> getOrdersByUser(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        if(orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found for user id :" + userId);
        }
        return orders.stream().map(order -> {
            OrderResponse response = modelMapper.map(order, OrderResponse.class);

            List<OrderItemResponse> itemsResp = order.getItems().stream().map(item -> {
                OrderItemResponse i = new OrderItemResponse();
                i.setProductId(item.getProductId());
                i.setProductName(item.getProductName());
                i.setQuantity(item.getQuantity());
                i.setPrice(item.getTotalPrice());
                return i;
            }).toList();

            response.setItems(itemsResp);
            response.setAddress(modelMapper.map(order.getAddress(), AddressResponse.class));
            response.setCreatedAt(order.getCreatedAt());
            return response;
        }).toList();
    }

    @Override
    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Order not found with id : " +id));
        order.setStatus("CANCELLED");
        orderRepository.save(order);

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
