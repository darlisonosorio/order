package br.com.darlison.order.domain.usecases;

import br.com.darlison.order.domain.enums.OrderStatus;
import br.com.darlison.order.domain.model.Order;
import br.com.darlison.order.domain.model.Product;
import br.com.darlison.order.domain.port.repository.OrderRepository;
import br.com.darlison.order.domain.port.usecase.ProcessOrderUseCase;

import java.math.BigDecimal;
import java.util.List;

public class ProcessOrderUseCaseImpl implements ProcessOrderUseCase {

    private final OrderRepository orderRepository;

    public ProcessOrderUseCaseImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void processPendingOrders() {
        List<Order> orders = orderRepository.get500ByStatus(OrderStatus.PENDING);
        orders.forEach(this::processPendingOrder);
    }

    private void processPendingOrder(Order order) {
        System.out.println("Processing order " + order.getOrderId());

        order.setStatus(OrderStatus.PROCESSING);
        orderRepository.save(order);

        order.getProducts().forEach(product -> {
            product.setTotalPrice(
                product.getPrice().multiply(BigDecimal.valueOf(product.getQuantity()))
            );
        });

        order.setTotalPrice(
            order.getProducts().stream()
                .map(Product::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        order.setStatus(OrderStatus.DONE);

        orderRepository.save(order);

        System.out.println("Order " + order.getOrderId() + " processed.");
    }
}
