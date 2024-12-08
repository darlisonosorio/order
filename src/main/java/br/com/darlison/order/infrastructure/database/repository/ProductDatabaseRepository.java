package br.com.darlison.order.infrastructure.database.repository;

import br.com.darlison.order.infrastructure.database.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductDatabaseRepository extends JpaRepository<ProductEntity, UUID> {

}
