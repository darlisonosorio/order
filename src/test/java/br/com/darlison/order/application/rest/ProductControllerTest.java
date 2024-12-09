package br.com.darlison.order.application.rest;

import br.com.darlison.order.domain.model.Product;
import br.com.darlison.order.domain.port.usecase.GetProductUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @MockBean
    private GetProductUseCase getProductUseCase;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product(
                UUID.randomUUID(), "Product1", 10,
                BigDecimal.valueOf(100.0), BigDecimal.valueOf(1000.0),
                null, null
        );
        product2 = new Product(
                UUID.randomUUID(), "Product2", 5, 
                BigDecimal.valueOf(200.0), BigDecimal.valueOf(1000.0), 
                null, null
        );
    }

    @Test
    void listProductsSuccess() throws Exception {
        Page<Product> pageResult = new PageImpl<>(List.of(product1, product2));
        Mockito.when(getProductUseCase.list("user@email.com", "Product", 0, 10))
                .thenReturn(pageResult);

        mockMvc.perform(get("/products")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("email", "user@email.com")
                        .param("product", "Product")
                        .param("page", "0")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Product1"))
                .andExpect(jsonPath("$.content[0].quantity").value(10))
                .andExpect(jsonPath("$.content[0].price").value(100.0))
                .andExpect(jsonPath("$.content[0].totalPrice").value(1000.0))
                .andExpect(jsonPath("$.content[1].name").value("Product2"))
                .andExpect(jsonPath("$.content[1].quantity").value(5))
                .andExpect(jsonPath("$.content[1].price").value(200.0))
                .andExpect(jsonPath("$.content[1].totalPrice").value(1000.0));
    }

    @Test
    void listProductsNoResults() throws Exception {
        Page<Product> pageResult = new PageImpl<>(Collections.emptyList());
        when(getProductUseCase.list("unknown@email.com", "Unknown", 0, 10))
                .thenReturn(pageResult);

        mockMvc.perform(get("/products")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("email", "unknown@email.com")
                        .param("product", "Unknown")
                        .param("page", "0")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    void getProductByIdSuccess() throws Exception {
        when(getProductUseCase.get(product1.getId())).thenReturn(Optional.of(product1));

        mockMvc.perform(get("/products/" + product1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Product1"))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.price").value(100.0))
                .andExpect(jsonPath("$.totalPrice").value(1000.0));
    }

    @Test
    void getProductByIdNotFound() throws Exception {
        UUID randomUUID = UUID.randomUUID();
        when(getProductUseCase.get(randomUUID)).thenReturn(Optional.empty());

        mockMvc.perform(get("/products/" + randomUUID))
                .andExpect(status().isNotFound());
    }
}
