package com.farmermart.orderservice.service;
import com.farmermart.orderservice.model.PurchasedProduct;
import com.farmermart.orderservice.repository.PurchasedProductRepository;
import com.farmermart.orderservice.service.Impl.PurchasedProductServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



class PurchasedProductServiceImplTest {

    @Mock
    private PurchasedProductRepository purchasedProductRepository;

    @InjectMocks
    private PurchasedProductServiceImpl purchasedProductService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPurchasedProductsByUser() {
        when(purchasedProductRepository.findByUserIdOrderByPurchasedCountDesc(1L))
                .thenReturn(List.of(new PurchasedProduct(1L, 2L, 5L)));

        List<PurchasedProduct> result = purchasedProductService.getPurchasedProductsByUser(1L);

        assertEquals(1, result.size());
        assertEquals(5L, result.get(0).getPurchasedCount());
    }
}
