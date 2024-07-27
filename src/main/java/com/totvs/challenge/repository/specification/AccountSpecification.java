package com.totvs.challenge.repository.specification;

import com.totvs.challenge.dto.AccountFilterDTO;
import com.totvs.challenge.model.Account;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AccountSpecification {

    public static Specification<Account> filterBy(AccountFilterDTO accountFilterDTO) {
        return Specification
                .where(hasDescription(accountFilterDTO.getDescription()))
                .and(hasDueDate(accountFilterDTO.getDueDate()))
                .and(hasPayDay(accountFilterDTO.getPayDay()))
                .and(hasStatus(accountFilterDTO.getStatus()));
    }

    private static Specification<Account> hasDescription(String description) {
        return ((root, query, cb) -> description == null || description.isEmpty() ? cb.conjunction() : cb.equal(root.get("description"), description));
    }

    private static Specification<Account> hasDueDate(LocalDate dueDate) {
        return (root, query, cb) -> dueDate == null ? cb.conjunction() : cb.equal(root.get("dueDate"), dueDate);
    }

    private static Specification<Account> hasPayDay(LocalDate payDay) {
        return (root, query, cb) -> payDay == null ? cb.conjunction() : cb.equal(root.get("payDay"), payDay);
    }

    private static Specification<Account> hasStatus(String status) {
        return (root, query, cb) -> status == null ? cb.conjunction() : cb.equal(root.get("status"), status);
    }
}
