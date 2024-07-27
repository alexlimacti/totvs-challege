package com.totvs.challenge.service;

import com.totvs.challenge.dto.AccountDTO;
import com.totvs.challenge.dto.AccountGetDTO;
import com.totvs.challenge.dto.AccountStatusDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface AccountService {

    void createAccount(AccountDTO accountDTO);

    void updateAccount(Long accountID, AccountDTO accountDTO);

    void changeStatus(Long accountID, AccountStatusDTO accountStatusDTO);

    void readAndSaveCSVAccounts(MultipartFile file) throws IOException;

    AccountGetDTO findAccountById(Long accountID);

    List<AccountGetDTO> findAccountsByDueDateBetween(LocalDate initialDate, LocalDate finalDate);

    List<AccountGetDTO> findAccountsByPayDayBetween(LocalDate initialDate, LocalDate finalDate);
}
