package br.com.darlison.order.infrastructure.database.repository;

import br.com.darlison.order.infrastructure.database.entity.ClientEntity;
import br.com.darlison.order.infrastructure.database.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderDatabaseRepository extends JpaRepository<OrderEntity, UUID> {

    boolean existsByOrderId(String orderId);

}
