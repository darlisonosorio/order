package br.com.darlison.order.application.model.output;

import java.math.BigDecimal;

public record ProductResponse(String name, int quantity, BigDecimal price, BigDecimal totalPrice) {
}
