package br.com.darlison.order.domain.adapters;

import br.com.darlison.order.domain.enums.OrderStatus;
import br.com.darlison.order.domain.model.Client;
import br.com.darlison.order.domain.model.Order;
import br.com.darlison.order.domain.model.Product;
import br.com.darlison.order.domain.port.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ProcessOrderUseCaseImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ProcessOrderUseCaseImpl processOrderUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void processPendingOrdersSuccess() {
        Order order = createValidOrder();

        when(orderRepository.get500ByStatus(OrderStatus.PENDING)).thenReturn(List.of(order));

        processOrderUseCase.processPendingOrders();

        BigDecimal expectedTotalPrice = BigDecimal.valueOf(250.0);
        assertEquals(expectedTotalPrice, order.getTotalPrice());

        assertEquals(OrderStatus.DONE, order.getStatus());

        verify(orderRepository, times(2)).save(order);
    }

    @Test
    void processPendingOrdersNoOrders() {
        when(orderRepository.get500ByStatus(OrderStatus.PENDING)).thenReturn(List.of());

        processOrderUseCase.processPendingOrders();

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void processPendingOrdersErrorDuringSave() {
        Order order = createValidOrder();

        when(orderRepository.get500ByStatus(OrderStatus.PENDING)).thenReturn(List.of(order));

        doThrow(new RuntimeException("Error while saving order")).when(orderRepository).save(any(Order.class));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> processOrderUseCase.processPendingOrders());

        assertEquals("Error while saving order", exception.getMessage());
    }

    private Order createValidOrder() {
        Product product1 = new Product(null, "Product1", 2, BigDecimal.valueOf(100.0), null, LocalDateTime.now(), LocalDateTime.now()); //200
        Product product2 = new Product(null, "Product2", 1, BigDecimal.valueOf(50.0), null, LocalDateTime.now(), LocalDateTime.now()); // 50
        return new Order(
            null,
            "123",
            OrderStatus.PENDING,
            null,
            new Client(),
            List.of(product1, product2),
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }
}
