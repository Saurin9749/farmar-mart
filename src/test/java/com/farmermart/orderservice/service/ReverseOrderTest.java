package com.farmermart.orderservice.service;
import com.farmermart.orderservice.dto.event.CustomerOrder;
import com.farmermart.orderservice.dto.event.OrderEvent;
import com.farmermart.orderservice.kafka.ReverseOrder;
import com.farmermart.orderservice.model.Order;
import com.farmermart.orderservice.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ReverseOrderTest {

    @Mock
    private OrderRepository repository;

    @InjectMocks
    private ReverseOrder reverseOrder;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void testReverseOrder_OrderExists_StatusUpdated() throws Exception {
        // Arrange: create a sample order and event
        Order order = new Order();
        order.setId(101L);
        order.setStatus("CREATED");

        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setOrderId(101L);

        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setOrder(customerOrder);
        orderEvent.setType("ORDER_REVERSED");

        String eventJson = objectMapper.writeValueAsString(orderEvent);

        when(repository.findById(101L)).thenReturn(Optional.of(order));

        // Act: call the listener method
        reverseOrder.reverseOrder(eventJson);

        // Assert: verify status updated and saved
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(repository).save(orderCaptor.capture());

        Order savedOrder = orderCaptor.getValue();
        assertEquals("FAILED", savedOrder.getStatus());

        // Verify findById called
        verify(repository).findById(101L);
    }

    @Test
    void testReverseOrder_OrderDoesNotExist() throws Exception {
        // Arrange: event for non-existing order
        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setOrderId(999L);

        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setOrder(customerOrder);
        orderEvent.setType("ORDER_REVERSED");

        String eventJson = objectMapper.writeValueAsString(orderEvent);

        when(repository.findById(999L)).thenReturn(Optional.empty());

        // Act
        reverseOrder.reverseOrder(eventJson);

        // Assert: repository.save should never be called
        verify(repository, never()).save(any(Order.class));
        verify(repository).findById(999L);
    }
}