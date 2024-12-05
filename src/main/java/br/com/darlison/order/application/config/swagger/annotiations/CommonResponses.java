package br.com.darlison.order.application.config.swagger.annotiations;

import br.com.darlison.order.common.exception.APIException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "Sucesso na requisição"
    ),
    @ApiResponse(
        responseCode = "400",
        description = "Requisição inválida",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = APIException.class)
        )
    ),
    @ApiResponse(
        responseCode = "401",
        description = "Não autorizado",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = APIException.class)
        )
    ),
    @ApiResponse(
        responseCode = "500",
        description = "Erro interno do Servidor",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = APIException.class)
        )
    ),
})
public @interface CommonResponses {
}
