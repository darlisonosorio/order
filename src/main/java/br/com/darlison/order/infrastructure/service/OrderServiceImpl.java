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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
            .map(this::toOrder).toList();
    }

    @Override
    public Optional<Order> getById(String id) {
        return repository.findByOrderId(id).map(this::toOrder);
    }

    @Override
    public Page<Order> listByUserEmailStatusOrProductName(String userEmail,
                                                          OrderStatus status,
                                                          String productName,
                                                          int page,
                                                          int limit) {
        return repository.findOrdersWithCustomFilters(
            userEmail,
            status,
            productName,
            PageRequest.of(page, limit)
        ).map(this::toOrder);
    }

    private Order toOrder(OrderEntity entity) {
        Order order = objectMapper.convertValue(entity, Order.class);
        order.setClient(objectMapper.convertValue(entity.getClientEntity(), Client.class));
        order.setProducts(
            entity.getProductEntities()
                .stream()
                .map((it) -> objectMapper.convertValue(it, Product.class))
                .toList()
        );
        return order;
    };

    private ProductEntity toProductEntity(Product product, OrderEntity orderEntity) {
        ProductEntity productEntity = objectMapper.convertValue(product, ProductEntity.class);
        productEntity.setOrderEntity(orderEntity);
        return productEntity;
    }
}
