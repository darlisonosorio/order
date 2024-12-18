package br.com.darlison.order.domain.usecases;

import br.com.darlison.order.domain.model.Product;
import br.com.darlison.order.domain.port.repository.ProductRepository;
import br.com.darlison.order.domain.port.usecase.GetProductUseCase;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public class GetProductUseCaseImpl implements GetProductUseCase {

    private final ProductRepository productRepository;

    public GetProductUseCaseImpl(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<Product> list(String userEmail, String productName, int page, int limit) {
        return productRepository.listByUserEmailOrProductName(
                userEmail,
                productName,
                page,
                limit
        );
    }

    @Override
    public Optional<Product> get(UUID id) {
        return productRepository.getById(id);
    }
}
