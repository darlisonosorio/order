package br.com.darlison.order.infrastructure.service;

import br.com.darlison.order.domain.model.Product;
import br.com.darlison.order.domain.port.repository.ProductRepository;
import br.com.darlison.order.infrastructure.database.entity.ProductEntity;
import br.com.darlison.order.infrastructure.database.repository.ProductDatabaseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductRepository {

    private final ProductDatabaseRepository repository;

    private ObjectMapper objectMapper;

    @Override
    public Optional<Product> getById(UUID id) {
        return repository.findById(id).map(it -> objectMapper.convertValue(it, Product.class));
    }

    @Override
    public Page<Product> listByUserEmailOrProductName(String userEmail, String productName, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("name").ascending());

        Page<ProductEntity> result = repository.findByUserEmailAndName(
                isNotEmpty(userEmail) ? userEmail : null,
                isNotEmpty(productName) ? productName : null,
                pageable
        );

        return result.map(it -> objectMapper.convertValue(it, Product.class));
    }

    private boolean isNotEmpty(String val) {
        return val != null && !val.trim().isEmpty();
    }

}
