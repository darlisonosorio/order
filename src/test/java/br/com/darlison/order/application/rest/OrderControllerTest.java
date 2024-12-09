package br.com.darlison.order.application.rest;

import br.com.darlison.order.application.config.kafka.KafkaTopicConfig;
import br.com.darlison.order.domain.enums.OrderStatus;
import br.com.darlison.order.domain.model.Client;
import br.com.darlison.order.domain.model.Order;
import br.com.darlison.order.domain.model.Product;
import br.com.darlison.order.domain.port.usecase.GetOrderUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @MockBean
    private KafkaTopicConfig kafkaTemplate;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetOrderUseCase getOrderUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private Order order;

    @BeforeEach
    void setUp() {
        Client client = new Client(
                UUID.randomUUID(),
                "Willen Defoe", "willen.defoe@email.com",
                LocalDateTime.now(), LocalDateTime.now()
        );

        Product product1 = new Product(null, "Product1", 2, BigDecimal.valueOf(100.0), BigDecimal.valueOf(200.0), null, null);
        Product product2 = new Product(null, "Product2", 1, BigDecimal.valueOf(50.0), BigDecimal.valueOf(50.0), null, null);

        order = new Order(null, "123", OrderStatus.PENDING, BigDecimal.valueOf(250.0), client, List.of(product1, product2), null, null);
    }

    @Test
    void listOrdersSuccess() throws Exception {
        Page<Order> pageResult = new PageImpl<>(List.of(order));
        when(getOrderUseCase.list("willen.defoe@email.com", OrderStatus.PENDING, "Product1", 0, 10))
                .thenReturn(pageResult);

        mockMvc.perform(get("/orders")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("email", "willen.defoe@email.com")
                        .param("status", "PENDING")
                        .param("product", "Product1")
                        .param("page", "0")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].orderId").value("123"))
                .andExpect(jsonPath("$.content[0].status").value("PENDING"))
                .andExpect(jsonPath("$.content[0].totalPrice").value(250.0))
                .andExpect(jsonPath("$.content[0].client.name").value("Willen Defoe"))
                .andExpect(jsonPath("$.content[0].client.email").value("willen.defoe@email.com"))
                .andExpect(jsonPath("$.content[0].products[0].name").value("Product1"))
                .andExpect(jsonPath("$.content[0].products[0].price").value(100.0))
                .andExpect(jsonPath("$.content[0].products[0].quantity").value(2))
                .andExpect(jsonPath("$.content[0].products[0].totalPrice").value(200.0))
                .andExpect(jsonPath("$.content[0].products[1].name").value("Product2"))
                .andExpect(jsonPath("$.content[0].products[1].price").value(50.0))
                .andExpect(jsonPath("$.content[0].products[1].quantity").value(1))
                .andExpect(jsonPath("$.content[0].products[1].totalPrice").value(50.0));
    }

    @Test
    void listOrdersNoResults() throws Exception {
        Page<Order> pageResult = new PageImpl<>(Collections.emptyList());
        when(getOrderUseCase.list(
                "usuario.desconhecido@email.com",
                OrderStatus.PENDING,
                "ProdutoDesconhecido",
                0, 10
        )).thenReturn(pageResult);

        mockMvc.perform(get("/orders")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .param("email", "usuario.desconhecido@email.com")
                .param("status", "PENDING")
                .param("product", "ProdutoDesconhecido")
                .param("page", "0")
                .param("limit", "10")
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    void getOrderByIdSuccess() throws Exception {
        when(getOrderUseCase.get("123")).thenReturn(Optional.of(order));

        mockMvc.perform(get("/orders/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value("123"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.totalPrice").value(250.0))
                .andExpect(jsonPath("$.client.name").value("Willen Defoe"))
                .andExpect(jsonPath("$.client.email").value("willen.defoe@email.com"))
                .andExpect(jsonPath("$.products[0].name").value("Product1"))
                .andExpect(jsonPath("$.products[0].price").value(100.0))
                .andExpect(jsonPath("$.products[0].quantity").value(2))
                .andExpect(jsonPath("$.products[0].totalPrice").value(200.0))
                .andExpect(jsonPath("$.products[1].name").value("Product2"))
                .andExpect(jsonPath("$.products[1].price").value(50.0))
                .andExpect(jsonPath("$.products[1].quantity").value(1))
                .andExpect(jsonPath("$.products[1].totalPrice").value(50.0));
    }

    @Test
    void getOrderByIdNotFound() throws Exception {
        when(getOrderUseCase.get("123")).thenReturn(Optional.empty());

        mockMvc.perform(get("/orders/123"))
                .andExpect(status().isNotFound());
    }
}