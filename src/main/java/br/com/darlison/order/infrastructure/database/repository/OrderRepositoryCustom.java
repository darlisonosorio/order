package br.com.darlison.order.infrastructure.database.repository;

import br.com.darlison.order.domain.enums.OrderStatus;
import br.com.darlison.order.infrastructure.database.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<OrderEntity> findOrdersWithCustomFilters(String email, OrderStatus status, String productName, Pageable pageable);
}
