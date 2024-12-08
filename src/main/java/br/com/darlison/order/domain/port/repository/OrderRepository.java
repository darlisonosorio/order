package br.com.darlison.order.domain.port.repository;

import br.com.darlison.order.domain.model.Order;

public interface OrderRepository {

    boolean containsId(String id);

    void save(Order order);

}
