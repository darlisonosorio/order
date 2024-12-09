package br.com.darlison.order.infrastructure.service;

import br.com.darlison.order.application.config.kafka.KafkaTopicConfig;
import br.com.darlison.order.domain.model.Product;
import br.com.darlison.order.infrastructure.database.entity.ClientEntity;
import br.com.darlison.order.infrastructure.database.entity.OrderEntity;
import br.com.darlison.order.infrastructure.database.entity.ProductEntity;
import br.com.darlison.order.infrastructure.database.repository.ClientDatabaseRepository;
import br.com.darlison.order.infrastructure.database.repository.OrderDatabaseRepository;
import br.com.darlison.order.infrastructure.database.repository.ProductDatabaseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ProductServiceImplTest {

    @MockBean
    private KafkaTopicConfig kafkaTemplate;

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private ProductDatabaseRepository repository;
    @Autowired
    private ClientDatabaseRepository clientRepository;
    @Autowired
    private OrderDatabaseRepository orderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ClientEntity clientEntity;
    private OrderEntity orderEntity;
    private ProductEntity productEntity1;
    private ProductEntity productEntity2;

    @BeforeEach
    void setUp() {
        clientEntity = new ClientEntity();
        clientEntity.setName("Client 1");
        clientEntity.setEmail("client1@example.com");
        clientRepository.save(clientEntity);

        orderEntity = new OrderEntity();
        orderEntity.setOrderId("123");
        orderEntity.setClientEntity(clientEntity);


        productEntity1 = new ProductEntity();
        productEntity1.setName("Product 1");
        productEntity1.setPrice(BigDecimal.valueOf(100));
        productEntity1.setQuantity(2);
        productEntity1.setTotalPrice(BigDecimal.valueOf(200));
        productEntity1.setOrderEntity(orderEntity);

        productEntity2 = new ProductEntity();
        productEntity2.setName("Product 2");
        productEntity2.setPrice(BigDecimal.valueOf(50));
        productEntity2.setQuantity(3);
        productEntity2.setTotalPrice(BigDecimal.valueOf(150));
        productEntity2.setOrderEntity(orderEntity);

        orderEntity.setProductEntities(List.of(productEntity1, productEntity2));

        orderRepository.save(orderEntity);
    }

    @Test
    void testGetById() {
        ProductEntity entity = repository.findAll().get(0);
        Optional<Product> result = productService.getById(entity.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo(entity.getName());
        assertThat(result.get().getPrice()).isEqualTo(entity.getPrice());
    }

    @Test
    void testListByUserEmailOrProductName() {

        Page<Product> result = productService.listByUserEmailOrProductName("client1@example.com", "Product", 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Product 1");
        assertThat(result.getContent().get(1).getName()).isEqualTo("Product 2");
    }

    @Test
    void testListByUserEmailOrProductNameValidOnlyOne() {

        Page<Product> result = productService.listByUserEmailOrProductName("client1@example.com", "2", 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Product 2");
    }

    @Test
    void testListByUserEmailOrProductNameEmptyEmail() {
        Page<Product> result = productService.listByUserEmailOrProductName(null, "Product 1", 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Product 1");
    }

    @Test
    void testListByUserEmailEmptyProductName() {
        Page<Product> result = productService.listByUserEmailOrProductName("client1@example.com", null, 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Product 1");
        assertThat(result.getContent().get(1).getName()).isEqualTo("Product 2");
    }

    @Test
    void testListByUserEmailInvalidEmail() {
        Page<Product> result = productService.listByUserEmailOrProductName("client2@example.com", null, 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(0);
    }
}