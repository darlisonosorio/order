package br.com.darlison.order.application.rest;

import br.com.darlison.order.application.config.swagger.annotiations.CommonResponses;
import br.com.darlison.order.application.model.output.OrderResponse;
import br.com.darlison.order.domain.enums.OrderStatus;
import br.com.darlison.order.domain.port.usecase.GetOrderUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
@Tag(name = "Orders", description = "API para obter informações dos pedidos")
public class OrderController {

    private final GetOrderUseCase getOrderUseCase;
    private final ObjectMapper objectMapper;

    @Operation(
            summary = "Listar pedidos",
            description = "Lista os pedidos com filtros opcionais, incluindo email do cliente, status do pedido e nome do produto."
    )
    @CommonResponses
    @GetMapping
    public Page<OrderResponse> list(
            @RequestParam(required = false) String userEmail,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) String productName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return getOrderUseCase.list(
                userEmail,
                status,
                productName,
                page,
                limit
        ).map(order -> objectMapper.convertValue(order, OrderResponse.class));
    }

    @Operation(
            summary = "Detalhar pedido",
            description = "Retorna os detalhes de um pedido específico dado seu ID."
    )
    @CommonResponses
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable String id) {
        return getOrderUseCase.get(id)
                .map(it -> objectMapper.convertValue(it, OrderResponse.class))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
