package br.com.darlison.order.infrastructure.service;

import br.com.darlison.order.application.config.kafka.KafkaTopicConfig;
import br.com.darlison.order.domain.enums.OrderStatus;
import br.com.darlison.order.domain.model.Client;
import br.com.darlison.order.domain.model.Order;
import br.com.darlison.order.domain.model.Product;
import br.com.darlison.order.infrastructure.database.entity.ClientEntity;
import br.com.darlison.order.infrastructure.database.repository.ClientDatabaseRepository;
import br.com.darlison.order.infrastructure.database.repository.OrderDatabaseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class OrderServiceImplTest {

    @MockBean
    private KafkaTopicConfig kafkaTemplate;

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private OrderDatabaseRepository repository;

    @Autowired
    private ClientDatabaseRepository clientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    void containsIdSuccess() {
        ClientEntity entity = new ClientEntity();
        entity.setName("Darlison Osorio");
        entity.setEmail("darlison.osorio@gmail.com");
        entity = clientRepository.save(entity);

        Order testOrder = new Order(
                null,
                "12345",
                OrderStatus.PENDING,
                null,
                new Client(entity.getId()),
                List.of(
                        new Product(null, "Monitor", 3, new BigDecimal("925.20"), null, LocalDateTime.now(), LocalDateTime.now()),
                        new Product(null, "Notebook", 2, new BigDecimal("3000.00"), null, LocalDateTime.now(), LocalDateTime.now())
                ),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        orderService.save(testOrder);

        boolean exists = orderService.containsId("12345");

        assertThat(exists).isTrue();
    }

    @Test
    void containsIdNotFound() {
        boolean exists = orderService.containsId("54321");
        assertThat(exists).isFalse();
    }

    @Test
    void saveOrderSuccess() {
        ClientEntity client = new ClientEntity();
        client.setName("Darlison Osorio");
        client.setEmail("darlison.osorio@gmail.com");
        client = clientRepository.save(client);

        Order testOrder = new Order(
                null,
                "12345",
                OrderStatus.PENDING,
                null,
                new Client(client.getId()),
                List.of(
                        new Product(null, "Monitor", 3, new BigDecimal("925.20"), null, LocalDateTime.now(), LocalDateTime.now()),
                        new Product(null, "Notebook", 2, new BigDecimal("3000.00"), null, LocalDateTime.now(), LocalDateTime.now())
                ),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        orderService.save(testOrder);

        var savedEntity = repository.findAll().stream().findFirst().orElseThrow();
        assertThat(repository.count()).isEqualTo(1);
        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getOrderId()).isEqualTo("12345");
        assertThat(savedEntity.getProductEntities()).hasSize(2);
        assertThat(savedEntity.getClientEntity().getId()).isEqualTo(client.getId());
    }

    @Test
    void saveOrderFailsForInvalidClient() {
        Order testOrder = new Order(
                null,
                "12345",
                OrderStatus.PENDING,
                null,
                new Client(null, null, null, LocalDateTime.now(), LocalDateTime.now()),
                List.of(
                        new Product(null, "Monitor", 3, new BigDecimal("925.20"), null, LocalDateTime.now(), LocalDateTime.now())
                ),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        assertThatThrownBy(() -> orderService.save(testOrder)).isInstanceOf(Exception.class);
    }

    @Test
    void get500ByStatusSuccess() {
        ClientEntity client = new ClientEntity();
        client.setName("Darlison Osorio");
        client.setEmail("darlison.osorio@gmail.com");
        client = clientRepository.save(client);

        for (int i = 0; i < 5; i++) {
            Order testOrder = new Order(
                    null,
                    "12345" + i,
                    OrderStatus.PENDING,
                    null,
                    new Client(client.getId()),
                    List.of(
                            new Product(null, "Monitor", 3, new BigDecimal("925.20"), null, LocalDateTime.now(), LocalDateTime.now())
                    ),
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
            orderService.save(testOrder);
        }

        List<Order> orders = orderService.get500ByStatus(OrderStatus.PENDING);

        assertThat(orders).hasSize(5);
        assertThat(orders.get(0).getOrderId()).startsWith("12345");
        assertThat(orders.get(0).getStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    void get500ByStatusNoOrders() {
        List<Order> orders = orderService.get500ByStatus(OrderStatus.PENDING);
        assertThat(orders).isEmpty();
    }

    @Test
    void get500ByStatusDifferentStatus() {
        ClientEntity client = new ClientEntity();
        client.setName("Darlison Osorio");
        client.setEmail("darlison.osorio@gmail.com");
        client = clientRepository.save(client);

        Order testOrder = new Order(
                null,
                "12345",
                OrderStatus.PENDING,
                null,
                new Client(client.getId()),
                List.of(
                        new Product(null, "Monitor", 3, new BigDecimal("925.20"), null, LocalDateTime.now(), LocalDateTime.now())
                ),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        orderService.save(testOrder);

        List<Order> orders = orderService.get500ByStatus(OrderStatus.PROCESSING);

        assertThat(orders).isEmpty();
    }

    @Test
    void get500ByStatusLimitTo500Orders() {
        ClientEntity client = new ClientEntity();
        client.setName("Darlison Osorio");
        client.setEmail("darlison.osorio@gmail.com");
        client = clientRepository.save(client);

        for (int i = 0; i < 500; i++) {
            Order testOrder = new Order(
                    null,
                    "12345" + i,
                    OrderStatus.PENDING,
                    null,
                    new Client(client.getId()),
                    List.of(
                            new Product(null, "Monitor", 3, new BigDecimal("925.20"), null, LocalDateTime.now(), LocalDateTime.now())
                    ),
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
            orderService.save(testOrder);
        }

        List<Order> orders = orderService.get500ByStatus(OrderStatus.PENDING);

        assertThat(orders).hasSize(500);
    }
}
