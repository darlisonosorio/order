package br.com.darlison.order.domain.dto;

import java.util.List;

public record OrderDto (String id, ClientDto client, List<ProductDto> produtos) {

}