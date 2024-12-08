package br.com.darlison.order.domain.port.usecase;

import br.com.darlison.order.domain.dto.OrderDto;

public interface SaveOrderUseCase {

    void save(OrderDto orderDto);

}
