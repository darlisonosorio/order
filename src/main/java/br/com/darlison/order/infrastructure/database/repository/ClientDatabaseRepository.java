package br.com.darlison.order.infrastructure.database.repository;

import br.com.darlison.order.infrastructure.database.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientDatabaseRepository extends JpaRepository<ClientEntity, UUID> {

    Optional<ClientEntity> findByEmail(String email);

}
