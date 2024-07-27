package com.totvs.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.totvs.challenge.model.Account;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("from Account account where account.dueDate between :dueDateStart and :dueDateEnd")
    List<Account> findAllByDueDateBeetween(LocalDate dueDateStart, LocalDate dueDateEnd);

    @Query("from Account account where account.dueDate between :payDayStart and :payDayEnd")
    List<Account> findAllByPayDayBeetween(LocalDate payDayStart, LocalDate payDayEnd);

}
