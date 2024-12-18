package br.com.darlison.order.domain.usecases;

import br.com.darlison.order.domain.dto.ClientDto;
import br.com.darlison.order.domain.dto.OrderDto;
import br.com.darlison.order.domain.dto.ProductDto;
import br.com.darlison.order.domain.enums.OrderError;
import br.com.darlison.order.domain.exceptions.SaveOrderException;
import br.com.darlison.order.domain.model.Client;
import br.com.darlison.order.domain.model.Order;
import br.com.darlison.order.domain.model.Product;
import br.com.darlison.order.domain.port.repository.ClientRepository;
import br.com.darlison.order.domain.port.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SaveOrderUseCaseImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private SaveOrderUseCaseImpl saveOrderUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveSuccessTest() {
        OrderDto orderDto = createValidOrder();

        Client mockClient = new Client(
            UUID.randomUUID(),
            "Willen Defoe", "willen.defoe@email.com",
            LocalDateTime.now(), LocalDateTime.now()
        );

        when(orderRepository.containsId(orderDto.id())).thenReturn(false);
        when(clientRepository.getOrCreate(any(Client.class))).thenReturn(mockClient);

        saveOrderUseCase.save(orderDto);

        verify(orderRepository).save(argThat(order -> {
            assertEquals("123", order.getOrderId());
            assertEquals(mockClient, order.getClient());
            assertNotNull(order.getProducts());
            assertEquals(2, order.getProducts().size());

            Product product1 = order.getProducts().get(0);
            assertEquals("Product1", product1.getName());
            assertEquals(2, product1.getQuantity());
            assertEquals(BigDecimal.valueOf(100.0), product1.getPrice());

            Product product2 = order.getProducts().get(1);
            assertEquals("Product2", product2.getName());
            assertEquals(1, product2.getQuantity());
            assertEquals(BigDecimal.valueOf(50.0), product2.getPrice());

            return true;
        }));
    }


    @Test
    void saveErrorOrderAlreadyExists() {
        OrderDto orderDto = createValidOrder();

        when(orderRepository.containsId(orderDto.id())).thenReturn(true);

        SaveOrderException exception = assertThrows(SaveOrderException.class, () -> saveOrderUseCase.save(orderDto));
        assertEquals(OrderError.ORDER_ALREADY_EXISTS.getMessage(), exception.getMessage());
    }

    @Test
    void saveErrorNoProductsFound() {
        OrderDto orderDto = new OrderDto(
            "123",
            new ClientDto("Willen Defoe", "willen.defoe@email.com"),
            Collections.emptyList()
        );

        when(orderRepository.containsId(orderDto.id())).thenReturn(false);

        SaveOrderException exception = assertThrows(SaveOrderException.class, () -> saveOrderUseCase.save(orderDto));
        assertEquals(OrderError.NO_PRODUCTS.getMessage(), exception.getMessage());
    }

    @Test
    void saveErrorWhileSavingClient() {
        OrderDto orderDto = createValidOrder();

        when(orderRepository.containsId(orderDto.id())).thenReturn(false);
        when(clientRepository.getOrCreate(any(Client.class))).thenThrow(new RuntimeException("Campo email é obrigatório"));

        SaveOrderException exception = assertThrows(SaveOrderException.class, () -> saveOrderUseCase.save(orderDto));
        assertEquals(OrderError.CLIENT_SAVE_ERROR.getMessage() + ": Campo email é obrigatório", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenOrderSaveFails() {
        OrderDto orderDto = createValidOrder();

        Client mockClient = new Client(
            UUID.randomUUID(),
            "Willen Defoe", "willen.defoe@email.com",
            LocalDateTime.now(), LocalDateTime.now()
        );

        when(orderRepository.containsId(orderDto.id())).thenReturn(false);
        when(clientRepository.getOrCreate(any(Client.class))).thenReturn(mockClient);
        doThrow(new RuntimeException("Campo order_id é obrigatório")).when(orderRepository).save(any(Order.class));

        SaveOrderException exception = assertThrows(SaveOrderException.class, () -> saveOrderUseCase.save(orderDto));
        assertEquals(OrderError.ORDER_SAVE_ERROR.getMessage() + ": Campo order_id é obrigatório", exception.getMessage());
    }

    private OrderDto createValidOrder() {
        return new OrderDto(
            "123",
            new ClientDto("Willen Defoe", "willen.defoe@email.com"),
            List.of(
                new ProductDto("Product1", 2, BigDecimal.valueOf(100.0)),
                new ProductDto("Product2", 1, BigDecimal.valueOf(50.0))
            )
        );
    }

}
