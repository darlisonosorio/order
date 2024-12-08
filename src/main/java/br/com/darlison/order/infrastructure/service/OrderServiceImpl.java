package br.com.darlison.order.infrastructure.service;

import br.com.darlison.order.domain.enums.OrderStatus;
import br.com.darlison.order.domain.model.Client;
import br.com.darlison.order.domain.model.Order;
import br.com.darlison.order.domain.model.Product;
import br.com.darlison.order.domain.port.repository.OrderRepository;
import br.com.darlison.order.infrastructure.database.entity.ClientEntity;
import br.com.darlison.order.infrastructure.database.entity.OrderEntity;
import br.com.darlison.order.infrastructure.database.entity.ProductEntity;
import br.com.darlison.order.infrastructure.database.repository.OrderDatabaseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Override
    @Transactional
    public List<Order> get500ByStatus(final OrderStatus status) {
        Pageable pageable = PageRequest.of(0, 500);
        return repository.findByStatus(status, pageable)
            .stream()
            .map(entity -> {
                Order order = objectMapper.convertValue(entity, Order.class);
                order.setClient(objectMapper.convertValue(entity.getClientEntity(), Client.class));
                order.setProducts(
                    entity.getProductEntities()
                          .stream()
                          .map((it) -> objectMapper.convertValue(it, Product.class))
                          .toList()
                );
                return order;
            }).toList();
    }

    private ProductEntity toProductEntity(Product product, OrderEntity orderEntity) {
        ProductEntity productEntity = objectMapper.convertValue(product, ProductEntity.class);
        productEntity.setOrderEntity(orderEntity);
        return productEntity;
    }
}
