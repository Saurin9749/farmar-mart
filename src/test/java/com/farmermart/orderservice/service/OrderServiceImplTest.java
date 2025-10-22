package com.farmermart.orderservice.service;
import com.farmermart.orderservice.dto.requestDto.*;
import com.farmermart.orderservice.dto.responseDto.*;
import com.farmermart.orderservice.exception.*;
import com.farmermart.orderservice.feign.*;
import com.farmermart.orderservice.model.*;
import com.farmermart.orderservice.model.Order;
import com.farmermart.orderservice.repository.*;
import com.farmermart.orderservice.service.Impl.OrderServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class OrderServiceImplTest {

    @Mock private OrderRepository orderRepository;
    @Mock private UserClient userClient;
    @Mock private ProductClient productClient;
    @Mock private AddressRepository addressRepository;
    @Mock private CartClient cartClient;
    @Mock private PurchasedProductRepository purchasedProductRepository;

    @InjectMocks private OrderServiceImpl orderService;

    private OrderRequest orderRequest;
    private ProductResponse productResponse;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderServiceImpl(orderRepository, userClient, productClient,
                new ModelMapper(), addressRepository, cartClient, purchasedProductRepository);

        productResponse = new ProductResponse();
        productResponse.setProductId(10L);
        productResponse.setName("Test Product");
        productResponse.setPrice(50.0);

        OrderItemRequest itemRequest = new OrderItemRequest(10L, 2, true);
        AddressRequest addressRequest = new AddressRequest("Street", "City", "State", "12345", "Country");
        orderRequest = new OrderRequest(1L, List.of(itemRequest), addressRequest);

    }

    @Test
    void testCreateOrder_ShouldSaveAndReturnOrderResponse() {
        when(userClient.validateUser(1L)).thenReturn(true);
        when(productClient.getProduct(10L)).thenReturn(productResponse);
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));
        when(purchasedProductRepository.findByUserIdAndProductId(1L, 10L))
                .thenReturn(Optional.empty());

        OrderResponse response = orderService.createOrder(orderRequest);

        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        assertEquals("PLACED", response.getStatus());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testCreateOrder_InvalidUser_ShouldThrowException() {
        when(userClient.validateUser(1L)).thenReturn(false);
        assertThrows(InvalidRequestException.class, () -> orderService.createOrder(orderRequest));
    }

    @Test
    void testGetOrderById_ShouldReturnResponse() {
        Order order = new Order();
        order.setId(100L);
        order.setUserId(1L);
        order.setStatus("PLACED");
        order.setAddress(new Address());
        order.setItems(List.of());

        when(orderRepository.findById(100L)).thenReturn(Optional.of(order));

        OrderResponse response = orderService.getOrderById(100L);
        assertEquals(100L, response.getOrderId());
    }
}
