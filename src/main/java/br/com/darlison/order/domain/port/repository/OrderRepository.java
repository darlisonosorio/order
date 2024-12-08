package br.com.darlison.order.domain.port.repository;

import br.com.darlison.order.domain.enums.OrderStatus;
import br.com.darlison.order.domain.model.Order;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    boolean containsId(String id);

    void save(Order order);

    List<Order> get500ByStatus(OrderStatus status);

    Optional<Order> getById(String id);

    Page<Order> listByUserEmailStatusOrProductName(String userEmail, OrderStatus status, String productName, int page, int limit);
}
