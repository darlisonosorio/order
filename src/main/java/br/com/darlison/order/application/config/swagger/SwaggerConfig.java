package br.com.darlison.order.application.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                description = "Serviço de gestão de Pedidos",
                version = "1.0.0",
                title = "Serviço de Pedidos"
        )
)
public class SwaggerConfig {

}
