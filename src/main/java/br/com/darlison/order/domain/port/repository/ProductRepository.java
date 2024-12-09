package br.com.darlison.order.domain.port.repository;

import br.com.darlison.order.domain.model.Product;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    Optional<Product> getById(UUID id);

    Page<Product> listByUserEmailOrProductName(String userEmail, String productName, int page, int limit);

}
