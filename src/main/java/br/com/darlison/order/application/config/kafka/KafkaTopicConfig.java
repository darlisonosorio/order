package br.com.darlison.order.application.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic ordersTopic() {
        System.out.println("kafka orders topic created");
        return new NewTopic("orders", 3, (short) 1);
    }

}