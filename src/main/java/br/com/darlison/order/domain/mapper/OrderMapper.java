package br.com.darlison.order.domain.mapper;

import br.com.darlison.order.domain.dto.OrderDto;
import br.com.darlison.order.domain.enums.OrderStatus;
import br.com.darlison.order.domain.model.Client;
import br.com.darlison.order.domain.model.Order;
import br.com.darlison.order.domain.model.Product;

import java.time.LocalDateTime;
import java.util.List;

public class OrderMapper {

    public static Order map(OrderDto orderDto, Client client, List<Product> products, OrderStatus status) {
        return new Order(
                null,
                orderDto.id(),
                status,
                null,
                client,
                products,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

}
