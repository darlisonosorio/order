package br.com.darlison.order.infrastructure.binds;

import br.com.darlison.order.domain.usecases.GetOrderUseCaseImpl;
import br.com.darlison.order.domain.usecases.GetProductUseCaseImpl;
import br.com.darlison.order.domain.usecases.ProcessOrderUseCaseImpl;
import br.com.darlison.order.domain.usecases.SaveOrderUseCaseImpl;
import br.com.darlison.order.domain.port.repository.ClientRepository;
import br.com.darlison.order.domain.port.repository.OrderRepository;
import br.com.darlison.order.domain.port.repository.ProductRepository;
import br.com.darlison.order.domain.port.usecase.GetOrderUseCase;
import br.com.darlison.order.domain.port.usecase.GetProductUseCase;
import br.com.darlison.order.domain.port.usecase.ProcessOrderUseCase;
import br.com.darlison.order.domain.port.usecase.SaveOrderUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesBindConfig {

    @Bean
    public SaveOrderUseCase bindSaveOrderUseCase(OrderRepository orderRepository, ClientRepository clientRepository) {
        return new SaveOrderUseCaseImpl(orderRepository, clientRepository);
    }

    @Bean
    public ProcessOrderUseCase bindProcessOrderUseCase(OrderRepository orderRepository) {
        return new ProcessOrderUseCaseImpl(orderRepository);
    }

    @Bean
    public GetOrderUseCase bindGetOrderUseCase(OrderRepository orderRepository) {
        return new GetOrderUseCaseImpl(orderRepository);
    }

    @Bean
    public GetProductUseCase bindGetProductUseCase(ProductRepository productRepository) {
        return new GetProductUseCaseImpl(productRepository);
    }

}
