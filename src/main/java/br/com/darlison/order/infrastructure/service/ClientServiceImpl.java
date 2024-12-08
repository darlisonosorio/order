package br.com.darlison.order.infrastructure.service;

import br.com.darlison.order.domain.model.Client;
import br.com.darlison.order.domain.port.repository.ClientRepository;
import br.com.darlison.order.infrastructure.database.entity.ClientEntity;
import br.com.darlison.order.infrastructure.database.repository.ClientDatabaseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientRepository {

    private final ClientDatabaseRepository repository;

    private ObjectMapper objectMapper;

    @Override
    public Client getOrCreate(Client client) {
        return repository.findByEmail(client.getEmail())
            .map(existingClientEntity -> objectMapper.convertValue(existingClientEntity, Client.class))
            .orElseGet(() -> createClient(client));
    }

    private Client createClient(Client client) {
        ClientEntity entity = objectMapper.convertValue(client, ClientEntity.class);
        ClientEntity savedEntity = repository.save(entity);
        return objectMapper.convertValue(savedEntity, Client.class);
    }
}
