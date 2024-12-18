package br.com.darlison.order.domain.usecases;

import br.com.darlison.order.domain.model.Product;
import br.com.darlison.order.domain.port.repository.ProductRepository;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class GetProductUseCaseImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private GetProductUseCaseImpl getProductUseCase;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("Product1");
        product.setQuantity(10);
        product.setPrice(BigDecimal.valueOf(100.0));
        product.setTotalPrice(BigDecimal.valueOf(1000.0));
    }

    @Test
    void getProductByIdSuccess() {
        when(productRepository.getById(product.getId())).thenReturn(Optional.of(product));

        Optional<Product> result = getProductUseCase.get(product.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(product.getId());
        assertThat(result.get().getName()).isEqualTo("Product1");
        assertThat(result.get().getPrice()).isEqualTo(BigDecimal.valueOf(100.0));
    }

    @Test
    void getProductByIdNotFound() {
        when(productRepository.getById(UUID.randomUUID())).thenReturn(Optional.empty());

        Optional<Product> result = getProductUseCase.get(UUID.randomUUID());

        assertThat(result).isEmpty();
    }

    @Test
    void listProductsWithFilters() {
        Page<Product> pageResult = new PageImpl<>(List.of(product));
        when(productRepository.listByUserEmailOrProductName("user@email.com", "Product1", 0, 10))
                .thenReturn(pageResult);

        Page<Product> result = getProductUseCase.list("user@email.com", "Product1", 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Product1");
    }

    @Test
    void listProductsWithNoFilters() {
        Page<Product> pageResult = new PageImpl<>(List.of(product));
        when(productRepository.listByUserEmailOrProductName(null, null, 0, 10))
                .thenReturn(pageResult);

        Page<Product> result = getProductUseCase.list(null, null, 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void listProductsWithNoResults() {
        Page<Product> pageResult = new PageImpl<>(Collections.emptyList());
        when(productRepository.listByUserEmailOrProductName("user@email.com", "Desconhecido", 0, 10))
                .thenReturn(pageResult);

        Page<Product> result = getProductUseCase.list("user@email.com", "Desconhecido", 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(0);
    }
}
