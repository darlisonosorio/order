package br.com.darlison.order.domain.mapper;

import br.com.darlison.order.domain.dto.ClientDto;
import br.com.darlison.order.domain.model.Client;

import java.time.LocalDateTime;

public class ClientMapper {

    public static Client map(ClientDto client) {
        return new Client(
                null,
                client.name(),
                client.email(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

}
