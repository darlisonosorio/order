package br.com.darlison.order.application.consumer;

import br.com.darlison.order.domain.dto.OrderDto;
import br.com.darlison.order.domain.exceptions.SaveOrderException;
import br.com.darlison.order.domain.port.usecase.SaveOrderUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderConsumer {

    private ObjectMapper objectMapper;

    private SaveOrderUseCase saveOrderUseCase;

    @KafkaListener(topics = "orders", groupId = "order-service-group", concurrency = "3")
    public void consume(final String message) {
        try {
            System.out.println("Pedido recebido: " + message);
            OrderDto orderDto = objectMapper.readValue(message, OrderDto.class);
            saveOrderUseCase.save(orderDto);
            System.out.println("Pedido inserido com sucesso.");
        } catch (JsonProcessingException e) {
            System.out.println("Erro durante a leitura do pedido. " + e.getMessage());
        }  catch (SaveOrderException e) {
            System.out.println("Pedido rejeitado: " + e.getMessage());
        }
    }

}