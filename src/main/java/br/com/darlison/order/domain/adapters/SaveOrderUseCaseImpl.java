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
import br.com.darlison.order.domain.port.usecase.SaveOrderUseCase;
import jakarta.transaction.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class SaveOrderUseCaseImpl implements SaveOrderUseCase {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;

    public SaveOrderUseCaseImpl(final OrderRepository orderRepository,
                                final ClientRepository clientRepository) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional
    public void save(final OrderDto orderDto) {
        validateOrder(orderDto);
        Client client = getOrCreateUser(orderDto.client());
        saveOrder(orderDto, client);
    }

    private void validateOrder(final OrderDto orderDto) {
        if (orderRepository.containsId(orderDto.id())) {
            throw new SaveOrderException(OrderError.ORDER_ALREADY_EXISTS.getMessage());
        }
        if (CollectionUtils.isEmpty(orderDto.produtos())) {
            throw new SaveOrderException(OrderError.NO_PRODUCTS.getMessage());
        }
    }

    private Client getOrCreateUser(final ClientDto clientDto) {
        try {
            Client client = ClientMapper.map(clientDto);
            return clientRepository.getOrCreate(client);
        } catch (Exception e) {
            throw new SaveOrderException(OrderError.CLIENT_SAVE_ERROR.getMessage() + ": " + e.getMessage());
        }
    }

    private void saveOrder(final OrderDto orderDto, final Client client) {
        try {
            final List<Product> products = orderDto.produtos().stream().map(ProductMapper::map).toList();
            final Order model = OrderMapper.map(
                orderDto,
                client,
                products,
                OrderStatus.PENDING
            );
            orderRepository.save(model);
        } catch (Exception e) {
            throw new SaveOrderException(OrderError.ORDER_SAVE_ERROR.getMessage() + ": " + e.getMessage());
        }
    }

}
