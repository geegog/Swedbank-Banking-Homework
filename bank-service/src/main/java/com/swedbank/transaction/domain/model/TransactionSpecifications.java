package com.swedbank.transaction.domain.model;

import com.swedbank.transaction.application.dto.TransactionSearch;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionSpecifications {
    public static Specification<Transaction> buildSearchQuery(TransactionSearch search, UUID authenticatedUserId,
                                                              String accountNumber) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("userId"), authenticatedUserId));

            predicates.add(criteriaBuilder.equal(root.get("accountNumber"), accountNumber));

            if (search.getTransactionTypes() != null && !search.getTransactionTypes().isEmpty()) {
                predicates.add(root.get("transactionType").in(search.getTransactionTypes()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
