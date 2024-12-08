package br.com.darlison.order.infrastructure.service;

import br.com.darlison.order.application.config.kafka.KafkaTopicConfig;
import br.com.darlison.order.domain.model.Client;
import br.com.darlison.order.infrastructure.database.entity.ClientEntity;
import br.com.darlison.order.infrastructure.database.repository.ClientDatabaseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ClientServiceImplTest {

    @MockBean
    private KafkaTopicConfig kafkaTemplate;

    @Autowired
    private ClientServiceImpl clientService;

    @Autowired
    private ClientDatabaseRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private Client testClient;

    @BeforeEach
    void setUp() {
        testClient = new Client(
            null,
            "Darlison Osorio",
            "darlison.osorio@gmail.com",
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    @Test
    void getOrCreateSuccess() {
        Client createdClient = clientService.getOrCreate(testClient);

        assertThat(createdClient).isNotNull();
        assertThat(createdClient.getName()).isEqualTo(testClient.getName());
        assertThat(createdClient.getEmail()).isEqualTo(testClient.getEmail());
        assertThat(repository.findByEmail(testClient.getEmail()).isPresent()).isTrue();
    }

    @Test
    void getOrCreateSuccessEmailExists() {
        var entity = new ClientEntity();
        entity.setEmail(testClient.getEmail());
        entity.setName(testClient.getName());
        repository.save(entity);

        Client existingClient = clientService.getOrCreate(testClient);

        assertThat(existingClient).isNotNull();
        assertThat(existingClient.getName()).isEqualTo(testClient.getName());
        assertThat(existingClient.getEmail()).isEqualTo(testClient.getEmail());
        assertThat(repository.findAll().size()).isEqualTo(1);
    }

}