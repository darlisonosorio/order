package br.com.darlison.order.domain.mapper;

import br.com.darlison.order.domain.dto.ProductDto;
import br.com.darlison.order.domain.model.Product;

import java.time.LocalDateTime;

public class ProductMapper {

    public static Product map(ProductDto product) {
        return new Product(
                null,
                product.name(),
                product.quantity(),
                product.price(),
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

}
