package br.com.darlison.order.application.rest;

import br.com.darlison.order.application.config.swagger.annotiations.CommonResponses;
import br.com.darlison.order.application.model.output.ProductResponse;
import br.com.darlison.order.domain.port.usecase.GetProductUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Products", description = "API para obter informações dos produtos")
@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

    private final GetProductUseCase getProductUseCase;
    private final ObjectMapper objectMapper;

    @Operation(
            summary = "Listar produtos",
            description = "Lista os produtos com filtros opcionais email do cliente e nome do produto."
    )
    @CommonResponses
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<ProductResponse> list(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String product,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return getProductUseCase.list(
                email,
                product,
                page,
                limit
        ).map(order -> objectMapper.convertValue(order, ProductResponse.class));
    }

    @Operation(
            summary = "Detalhar produto",
            description = "Retorna os detalhes de um produto específico dado seu ID."
    )
    @CommonResponses
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable UUID id) {
        return getProductUseCase.get(id)
                .map(it -> objectMapper.convertValue(it, ProductResponse.class))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
