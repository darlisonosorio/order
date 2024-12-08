package br.com.darlison.order.application.consumer;


import br.com.darlison.order.domain.dto.ClientDto;
import br.com.darlison.order.domain.dto.OrderDto;
import br.com.darlison.order.domain.dto.ProductDto;
import br.com.darlison.order.domain.exceptions.SaveOrderException;
import br.com.darlison.order.domain.port.usecase.SaveOrderUseCase;
import br.com.darlison.order.infrastructure.configuration.JacksonConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@EnableKafka
@SpringJUnitConfig(classes = JacksonConfig.class)
@EmbeddedKafka(partitions = 1, topics = "orders")
public class OrderConsumerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private SaveOrderUseCase saveOrderUseCase;

    private OrderConsumer orderConsumer;

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();


    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        orderConsumer = new OrderConsumer(objectMapper, saveOrderUseCase);
    }

    @Test
    void consumeSuccessTest() throws Exception {
        String jsonMessage = createValidMessage();

        OrderDto orderDto = new OrderDto(
        "23234",
            new ClientDto("Darlison Osorio", "darlison.osorio@gmail.com"),
            List.of(
              new ProductDto("Monitor", 3, new BigDecimal("925.20")),
              new ProductDto("Nodebook", 2, new BigDecimal("3000.00"))
            )
        );

        orderConsumer.consume(jsonMessage);

        verify(saveOrderUseCase).save(argThat(result -> {
            assertEquals(orderDto.id(), result.id());
            assertEquals(orderDto.client().name(), result.client().name());
            assertEquals(orderDto.client().email(), result.client().email());

            assertEquals(orderDto.produtos().size(), result.produtos().size());

            assertEquals(orderDto.produtos().get(0).name(), result.produtos().get(0).name());
            assertEquals(orderDto.produtos().get(0).price().doubleValue(), result.produtos().get(0).price().doubleValue());
            assertEquals(orderDto.produtos().get(0).quantity(), result.produtos().get(0).quantity());

            return true;
        }));
    }

    @Test
    void consumeErrorInvalidMessageFormatTest() {
        String invalidMessage = "[ " + createValidMessage() + " ]";

        orderConsumer.consume(invalidMessage);

        verify(saveOrderUseCase, never()).save(any());

        assertTrue(outputStreamCaptor.toString().contains("Erro durante a leitura do pedido."));
    }

    @Test
    void testConsume_saveOrderUseCaseThrowsException() {
        String jsonMessage = createValidMessage();

        doThrow(new SaveOrderException("Erro ao salvar o pedido")).when(saveOrderUseCase).save(any());

        orderConsumer.consume(jsonMessage);

        assertTrue(outputStreamCaptor.toString().contains("Pedido rejeitado: Erro ao salvar o pedido"));
    }

    private String createValidMessage() {
        return """
            {
                "id": "23234",
                "client":{ "name": "Darlison Osorio", "email": "darlison.osorio@gmail.com" },
                "produtos": [
                  { "name": "Monitor", "quantity":3, "price":925.20 },
                  { "name": "Notebook", "quantity":2, "price":3000.00 }
                ]
            }
        """;
    }
}
