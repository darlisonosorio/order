package br.com.darlison.order.infrastructure.database.repository;

import br.com.darlison.order.domain.enums.OrderStatus;
import br.com.darlison.order.domain.model.Order;
import br.com.darlison.order.infrastructure.database.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderDatabaseRepository extends JpaRepository<OrderEntity, UUID>, OrderRepositoryCustom {


    Page<OrderEntity> findByStatus(OrderStatus status, Pageable pageable);

    boolean existsByOrderId(String orderId);

    Optional<OrderEntity> findByOrderId(String id);
}
