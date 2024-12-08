package br.com.darlison.order.infrastructure.service;

import br.com.darlison.order.domain.model.Order;
import br.com.darlison.order.domain.model.Product;
import br.com.darlison.order.domain.port.repository.OrderRepository;
import br.com.darlison.order.infrastructure.database.entity.ClientEntity;
import br.com.darlison.order.infrastructure.database.entity.OrderEntity;
import br.com.darlison.order.infrastructure.database.entity.ProductEntity;
import br.com.darlison.order.infrastructure.database.repository.OrderDatabaseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderRepository {

    private final OrderDatabaseRepository repository;

    private ObjectMapper objectMapper;

    @Override
    public boolean containsId(String id) {
        return repository.existsByOrderId(id);
    }

    @Override
    public void save(Order order) {
        OrderEntity orderEntity = objectMapper.convertValue(order, OrderEntity.class);

        orderEntity.setClientEntity(new ClientEntity(order.getClient().getId()));
        orderEntity.setProductEntities(
                order.getProducts()
                        .stream()
                        .map(product -> toProductEntity(product, orderEntity))
                        .toList());

        repository.save(orderEntity);
    }

    private ProductEntity toProductEntity(Product product, OrderEntity orderEntity) {
        ProductEntity productEntity = objectMapper.convertValue(product, ProductEntity.class);
        productEntity.setOrderEntity(orderEntity);
        return productEntity;
    }
}
