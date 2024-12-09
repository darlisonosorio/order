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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class GetOrderUseCaseImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private GetOrderUseCaseImpl getOrderUseCase;

    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Client client = new Client();
        client.setEmail("willen.defoe@email.com");
        client.setName("Willen Defoe");

        Product product1 = new Product();
        product1.setName("Product1");
        product1.setQuantity(2);
        product1.setPrice(BigDecimal.valueOf(100.0));

        Product product2 = new Product();
        product2.setName("Product2");
        product2.setQuantity(1);
        product2.setPrice(BigDecimal.valueOf(50.0));

        order = new Order();
        order.setOrderId("123");
        order.setStatus(OrderStatus.PENDING);
        order.setClient(client);
        order.setProducts(List.of(product1, product2));
    }

    @Test
    void getOrderByIdSuccess() {
        when(orderRepository.getById("123")).thenReturn(Optional.of(order));

        Optional<Order> result = getOrderUseCase.get("123");

        assertThat(result).isPresent();
        assertThat(result.get().getOrderId()).isEqualTo("123");
        assertThat(result.get().getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(result.get().getClient().getEmail()).isEqualTo("willen.defoe@email.com");
    }

    @Test
    void getOrderByIdNotFound() {
        when(orderRepository.getById("123")).thenReturn(Optional.empty());

        Optional<Order> result = getOrderUseCase.get("123");

        assertThat(result).isEmpty();
    }

    @Test
    void listOrdersWithFilters() {
        Page<Order> pageResult = new PageImpl<>(List.of(order));
        when(orderRepository.listByUserEmailStatusOrProductName("willen.defoe@email.com", OrderStatus.PENDING, "Product1", 0, 10))
                .thenReturn(pageResult);

        Page<Order> result = getOrderUseCase.list("willen.defoe@email.com", OrderStatus.PENDING, "Product1", 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getOrderId()).isEqualTo("123");
    }

    @Test
    void listOrdersWithNoFilters() {
        Page<Order> pageResult = new PageImpl<>(List.of(order));
        when(orderRepository.listByUserEmailStatusOrProductName(null, null, null, 0, 10))
                .thenReturn(pageResult);

        Page<Order> result = getOrderUseCase.list(null, null, null, 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void listOrdersWithNoResults() {
        Page<Order> pageResult = new PageImpl<>(Collections.emptyList());
        when(orderRepository.listByUserEmailStatusOrProductName("user.desconhecido@gmail.com", OrderStatus.PENDING, "ProductDesconhecido", 0, 10))
                .thenReturn(pageResult);

        Page<Order> result = getOrderUseCase.list("user.desconhecido@gmail.com", OrderStatus.PENDING, "ProductDesconhecido", 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(0);
    }
}