package com.totvs.challenge.service;

import com.totvs.challenge.dto.AccountDTO;
import com.totvs.challenge.dto.AccountStatusDTO;
import com.totvs.challenge.model.Account;
import com.totvs.challenge.repository.AccountRepository;
import com.totvs.challenge.service.impl.AccountServiceImpl;
import com.totvs.challenge.utils.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest extends BaseTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;

    @Spy
    private ModelMapper modelMapper;

    private final Long ID = 1L;
    private final String PATH_CREATE_ACCOUNT_JSON = "json/createAccount.json";
    private final String PATH_UPDATE_ACCOUNT_JSON = "json/updateAccount.json";
    private final String PATH_CHANGE_STATUS_JSON = "json/changeStatus.json";
    private final String PATH_ACCOUNT_JSON = "json/account.json";
    private final String PATH_CSV_FILE = "file/conta.csv";
    private final LocalDate INITIAL_FINAL_DATE = LocalDate.parse("2024-04-16");

    @Test
    void shouldCreateAccount() {
        var createAccount = createObjectFromClass(PATH_CREATE_ACCOUNT_JSON, AccountDTO.class);
        accountService.createAccount(createAccount);
        verify(accountRepository).save(any());
    }

    @Test
    void shouldUpdateAccount() {
        var updateAccount = createObjectFromClass(PATH_UPDATE_ACCOUNT_JSON, AccountDTO.class);
        var account = createObjectFromClass(PATH_ACCOUNT_JSON, Account.class);
        when(accountRepository.findById(ID)).thenReturn(Optional.of(account));
        accountService.updateAccount(ID, updateAccount);
        verify(accountRepository).save(any());
        assertEquals(updateAccount.getStatus(), account.getStatus());
        assertEquals(updateAccount.getDescription(), account.getDescription());
    }

    @Test
    void shouldChangeStatus() {
        var changeAccount = createObjectFromClass(PATH_CHANGE_STATUS_JSON, AccountStatusDTO.class);
        var account = createObjectFromClass(PATH_ACCOUNT_JSON, Account.class);
        when(accountRepository.findById(ID)).thenReturn(Optional.of(account));
        accountService.changeStatus(ID, changeAccount);
        verify(accountRepository).save(any());
        assertEquals(changeAccount.getStatus(), account.getStatus());
    }

    @Test
    void shouldFindAccountById() {
        var account = createObjectFromClass(PATH_ACCOUNT_JSON, Account.class);
        when(accountRepository.findById(ID)).thenReturn(Optional.of(account));
        var accountDTO = accountService.findAccountById(ID);
        verify(accountRepository).findById(ID);
        assertEquals(account.getStatus(), accountDTO.getStatus());
        assertEquals(account.getId(), accountDTO.getId());
        assertEquals(account.getDescription(), accountDTO.getDescription());
        assertEquals(account.getDueDate(), accountDTO.getDueDate());
        assertEquals(account.getPayDay(), accountDTO.getPayDay());
    }

    @Test
    void shouldFindAccountsByDueDateBetween() {
        var account = createObjectFromClass(PATH_ACCOUNT_JSON, Account.class);
        when(accountRepository.findAllByDueDateBeetween(INITIAL_FINAL_DATE, INITIAL_FINAL_DATE)).thenReturn(List.of(account));
        var accountsDTO = accountService.findAccountsByDueDateBetween(INITIAL_FINAL_DATE, INITIAL_FINAL_DATE);
        verify(accountRepository).findAllByDueDateBeetween(INITIAL_FINAL_DATE, INITIAL_FINAL_DATE);
        assertEquals(1, accountsDTO.size());
    }

    @Test
    void shouldFindAccountsByPayDayBetween() {
        var account = createObjectFromClass(PATH_ACCOUNT_JSON, Account.class);
        when(accountRepository.findAllByPayDayBeetween(INITIAL_FINAL_DATE, INITIAL_FINAL_DATE)).thenReturn(List.of(account));
        var accountsDTO = accountService.findAccountsByPayDayBetween(INITIAL_FINAL_DATE, INITIAL_FINAL_DATE);
        verify(accountRepository).findAllByPayDayBeetween(INITIAL_FINAL_DATE, INITIAL_FINAL_DATE);
        assertEquals(1, accountsDTO.size());
    }
    @Test
    void shouldReadAndSaveCSVAccounts() throws IOException {
        var filePath = new ClassPathResource(PATH_CSV_FILE).getPath();
        var multipartFile = new MockMultipartFile(filePath, "", "application/csv", "{\"key1\": \"value1\"}".getBytes());
        accountService.readAndSaveCSVAccounts(multipartFile);
        verify(accountRepository).saveAll(any());
    }



}
