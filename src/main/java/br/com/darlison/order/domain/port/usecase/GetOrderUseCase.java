package br.com.darlison.order.domain.port.usecase;

import br.com.darlison.order.domain.enums.OrderStatus;
import br.com.darlison.order.domain.model.Order;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface GetOrderUseCase {

    Page<Order> list(String userEmail, OrderStatus status, String productName, int page, int limit);

    Optional<Order> get(String id);

}
