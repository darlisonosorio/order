package br.com.darlison.order.domain.port.repository;

import br.com.darlison.order.domain.model.Client;

public interface ClientRepository {

    Client getOrCreate(Client client);

}
