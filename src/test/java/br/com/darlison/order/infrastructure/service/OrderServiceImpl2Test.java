package br.com.darlison.order.infrastructure.service;

import br.com.darlison.order.application.config.kafka.KafkaTopicConfig;
import br.com.darlison.order.domain.enums.OrderStatus;
import br.com.darlison.order.domain.model.Client;
import br.com.darlison.order.domain.model.Order;
import br.com.darlison.order.domain.model.Product;
import br.com.darlison.order.infrastructure.database.entity.ClientEntity;
import br.com.darlison.order.infrastructure.database.entity.OrderEntity;
import br.com.darlison.order.infrastructure.database.entity.ProductEntity;
import br.com.darlison.order.infrastructure.database.repository.ClientDatabaseRepository;
import br.com.darlison.order.infrastructure.database.repository.OrderDatabaseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class OrderServiceImpl2Test {

    @MockBean
    private KafkaTopicConfig kafkaTemplate;

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private OrderDatabaseRepository repository;

    @Autowired
    private ClientDatabaseRepository clientRepository;

    @BeforeEach
    void setUp() {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setEmail("darlison.osorio@gmail.com");
        clientEntity.setName("Darlison Osorio");
        clientRepository.save(clientEntity);

        OrderEntity order1 = new OrderEntity();
        order1.setOrderId("12345");
        order1.setStatus(OrderStatus.PENDING);
        order1.setClientEntity(clientEntity);
        order1.setProductEntities(List.of(new ProductEntity("Product", new BigDecimal("1"), 1, null, order1)));
        repository.save(order1);

        OrderEntity order2 = new OrderEntity();
        order2.setOrderId("67890");
        order2.setStatus(OrderStatus.PENDING);
        order2.setClientEntity(clientEntity);
        order2.setProductEntities(List.of(new ProductEntity("Product2", new BigDecimal("1"), 1, null, order2)));
        repository.save(order2);
    }

    @Test
    void getByIdSuccess() {
        Optional<Order> result = orderService.getById("12345");

        assertThat(result).isPresent();
        assertThat(result.get().getOrderId()).isEqualTo("12345");
        assertThat(result.get().getStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    void getByIdNotFound() {
        Optional<Order> result = orderService.getById("id-desconhecido");
        assertThat(result).isEmpty();
    }

    @Test
    void listByUserEmailStatusOrProductName_withFilters() {
        Page<Order> result = orderService.listByUserEmailStatusOrProductName(
                "darlison.osorio@gmail.com", OrderStatus.PENDING, "Product", 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    @Test
    void listByUserEmailStatusOrProductName_withNoFilters() {
        Page<Order> result = orderService.listByUserEmailStatusOrProductName(
                null, null, null, 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    @Test
    void listByUserEmailStatusOrProductName_emptyResults() {
        Page<Order> result = orderService.listByUserEmailStatusOrProductName(
                "maria.clara@gmail.com", OrderStatus.PENDING, "ProdutoDesconhecido", 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(0);
    }
}
