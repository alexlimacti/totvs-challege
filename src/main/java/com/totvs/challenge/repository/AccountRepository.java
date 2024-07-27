package com.totvs.challenge.repository;

import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.totvs.challenge.model.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("from Account account where account.dueDate between :dueDateStart and :dueDateEnd")
    List<Account> findAllByDueDateBeetween(LocalDate dueDateStart, LocalDate dueDateEnd);

    @Query("from Account account where account.dueDate between :payDayStart and :payDayEnd")
    List<Account> findAllByPayDayBeetween(LocalDate payDayStart, LocalDate payDayEnd);

    Page<Account> findAll(@Nullable Specification<Account> spec, @NonNull Pageable pageable);

}
