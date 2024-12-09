package br.com.darlison.order.domain.port.usecase;

import br.com.darlison.order.domain.model.Product;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface GetProductUseCase {

    Page<Product> list(String userEmail, String productName, int page, int limit);

    Optional<Product> get(UUID id);

}
