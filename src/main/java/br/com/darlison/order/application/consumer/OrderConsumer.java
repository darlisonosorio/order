package br.com.darlison.order.application.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    @KafkaListener(topics = "orders", groupId = "order-service-group")
    public void consume(String orderMessage) {
        System.out.println("Pedido recebido: " + orderMessage);
        // Processar e calcular o pedido
    }
}