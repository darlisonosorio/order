package br.com.darlison.order.domain.dto;

import java.math.BigDecimal;

public record ProductDto(String name, int quantity, BigDecimal price) {

}