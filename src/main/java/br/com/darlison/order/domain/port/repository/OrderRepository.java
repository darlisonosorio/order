package br.com.darlison.order.domain.port.repository;

import br.com.darlison.order.domain.enums.OrderStatus;
import br.com.darlison.order.domain.model.Order;

import java.util.List;

public interface OrderRepository {

    boolean containsId(String id);

    void save(Order order);

    List<Order> get500ByStatus(OrderStatus status);

}
