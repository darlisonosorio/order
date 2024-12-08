package br.com.darlison.order.application.model.output;


import br.com.darlison.order.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponse(String orderId, OrderStatus status, BigDecimal totalPrice, ClientResponse client,
                            List<ProductResponse> products) {

}
