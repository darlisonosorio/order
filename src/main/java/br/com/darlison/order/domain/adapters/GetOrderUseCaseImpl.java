package br.com.darlison.order.domain.adapters;

import br.com.darlison.order.domain.enums.OrderStatus;
import br.com.darlison.order.domain.model.Order;
import br.com.darlison.order.domain.port.repository.OrderRepository;
import br.com.darlison.order.domain.port.usecase.GetOrderUseCase;
import org.springframework.data.domain.Page;

import java.util.Optional;

public class GetOrderUseCaseImpl implements GetOrderUseCase {

    private final OrderRepository orderRepository;

    public GetOrderUseCaseImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Page<Order> list(String userEmail, OrderStatus status, String productName, int page, int limit) {
        return orderRepository.listByUserEmailStatusOrProductName(
                userEmail,
                status,
                productName,
                page,
                limit
        );
    }

    @Override
    public Optional<Order> get(String id) {
        return orderRepository.getById(id);
    }
}
