package br.com.darlison.order.infrastructure.binds;

import br.com.darlison.order.domain.adapters.SaveOrderUseCaseImpl;
import br.com.darlison.order.domain.port.repository.ClientRepository;
import br.com.darlison.order.domain.port.repository.OrderRepository;
import br.com.darlison.order.domain.port.usecase.SaveOrderUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesBindConfig {

    @Bean
    public SaveOrderUseCase bindSaveOrderUseCase(OrderRepository orderRepository, ClientRepository clientRepository) {
        return new SaveOrderUseCaseImpl(orderRepository, clientRepository);
    }

}
