package br.com.darlison.order.infrastructure.database.repository;

import br.com.darlison.order.infrastructure.database.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductDatabaseRepository extends JpaRepository<ProductEntity, UUID>  {

    @Query("""
        SELECT p FROM tb_product p
        JOIN p.orderEntity o
        JOIN o.clientEntity c
        WHERE (:email IS NULL OR c.email = :email)
        AND (:name IS NULL OR p.name LIKE %:name%)
    """)
    Page<ProductEntity> findByUserEmailAndName(String email, String name, Pageable pageable);

}
