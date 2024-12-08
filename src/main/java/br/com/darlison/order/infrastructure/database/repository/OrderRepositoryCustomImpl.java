package br.com.darlison.order.infrastructure.database.repository;

import br.com.darlison.order.domain.enums.OrderStatus;
import br.com.darlison.order.infrastructure.database.entity.ClientEntity;
import br.com.darlison.order.infrastructure.database.entity.OrderEntity;
import br.com.darlison.order.infrastructure.database.entity.ProductEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<OrderEntity> findOrdersWithCustomFilters(String email, OrderStatus status, String productName, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderEntity> cq = cb.createQuery(OrderEntity.class);
        Root<OrderEntity> root = cq.from(OrderEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        if (email != null && !email.trim().isEmpty()) {
            Join<OrderEntity, ClientEntity> clientJoin = root.join("clientEntity", JoinType.INNER);
            predicates.add(cb.equal(clientJoin.get("email"), email));
        }

        if (status != null) {
            predicates.add(cb.equal(root.get("status"), status));
        }

        if (productName != null && !productName.trim().isEmpty()) {
            Join<OrderEntity, ProductEntity> productJoin = root.join("productEntities", JoinType.INNER);
            predicates.add(cb.like(cb.lower(productJoin.get("name")), "%" + productName.toLowerCase() + "%"));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        TypedQuery<OrderEntity> query = entityManager.createQuery(cq);

        int totalRecords = query.getResultList().size();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<OrderEntity> result = query.getResultList();
        return new PageImpl<>(result, pageable, totalRecords);
    }
}