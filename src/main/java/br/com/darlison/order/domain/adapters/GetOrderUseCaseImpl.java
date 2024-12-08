package br.com.darlison.order.domain.adapters;

import br.com.darlison.order.domain.dto.ClientDto;
import br.com.darlison.order.domain.dto.OrderDto;
import br.com.darlison.order.domain.enums.OrderError;
import br.com.darlison.order.domain.enums.OrderStatus;
import br.com.darlison.order.domain.exceptions.SaveOrderException;
import br.com.darlison.order.domain.mapper.ClientMapper;
import br.com.darlison.order.domain.mapper.OrderMapper;
import br.com.darlison.order.domain.mapper.ProductMapper;
import br.com.darlison.order.domain.model.Client;
import br.com.darlison.order.domain.model.Order;
import br.com.darlison.order.domain.model.Product;
import br.com.darlison.order.domain.port.repository.ClientRepository;
import br.com.darlison.order.domain.port.repository.OrderRepository;
import br.com.darlison.order.domain.port.usecase.GetOrderUseCase;
import br.com.darlison.order.domain.port.usecase.SaveOrderUseCase;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;

import java.util.List;
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
