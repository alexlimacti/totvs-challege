package com.totvs.challenge.service.impl;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.totvs.challenge.dto.AccountCsvDTO;
import com.totvs.challenge.dto.AccountDTO;
import com.totvs.challenge.dto.AccountFilterDTO;
import com.totvs.challenge.dto.AccountGetDTO;
import com.totvs.challenge.dto.AccountPageResponse;
import com.totvs.challenge.dto.AccountStatusDTO;
import com.totvs.challenge.exception.AccountNotFoundException;
import com.totvs.challenge.model.Account;
import com.totvs.challenge.repository.AccountRepository;
import com.totvs.challenge.repository.specification.AccountSpecification;
import com.totvs.challenge.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    public void createAccount(AccountDTO accountDTO) {
        log.info("AccountServiceImpl.createAccount - start");
        var account = modelMapper.map(accountDTO, Account.class);
        accountRepository.save(account);
        log.info("AccountServiceImpl.createAccount - finish");
    }

    public void updateAccount(Long accountID, AccountDTO accountDTO) {
        log.info("AccountServiceImpl.updatingAccount - start");
        var account = getAccountById(accountID);
        modelMapper.map(accountDTO, account);
        accountRepository.save(account);
        log.info("AccountServiceImpl.updatingAccount - finish");
    }

    public AccountGetDTO findAccountById(Long accountID) {
        return modelMapper.map(getAccountById(accountID), AccountGetDTO.class);
    }

    public List<AccountGetDTO> findAccountsByDueDateBetween(LocalDate initialDate, LocalDate finalDate) {
        var accounts = accountRepository.findAllByDueDateBeetween(initialDate, finalDate);
        return mapAccountToAccountGetDTO(accounts);
    }

    public List<AccountGetDTO> findAccountsByPayDayBetween(LocalDate initialDate, LocalDate finalDate) {
        var accounts = accountRepository.findAllByPayDayBeetween(initialDate, finalDate);
        return mapAccountToAccountGetDTO(accounts);
    }

    private Account getAccountById(Long accountID){
        return accountRepository.findById(accountID).orElseThrow(() -> new AccountNotFoundException(accountID));
    }

    public void changeStatus(Long accountID, AccountStatusDTO accountStatusDTO) {
        log.info("AccountServiceImpl.changeStatus - start");
        var account = getAccountById(accountID);
        account.setStatus(accountStatusDTO.getStatus());
        accountRepository.save(account);
        log.info("AccountServiceImpl.changeStatus - finish");
    }

    public AccountPageResponse<AccountGetDTO> findByFilter(AccountFilterDTO filter, int page, int size) {
        var spec = AccountSpecification.filterBy(filter);
        var pageResult = accountRepository.findAll(spec, PageRequest.of(page, size));
        return mapAccountToResponse(pageResult);
    }

    public void readAndSaveCSVAccounts(MultipartFile file) throws IOException {
        log.info("AccountServiceImpl.readAndSaveCSVAccounts - start");
        if (file.isEmpty())
            throw new IllegalStateException("Cannot upload empty file");
        
        var accounts = mapAccountCSVToModel(convertCsvToModel(file,AccountCsvDTO.class));
        accountRepository.saveAll(accounts);
        log.info("AccountServiceImpl.readAndSaveCSVAccounts - finish");
    }
    
    private <T> List<T> convertCsvToModel(MultipartFile file, Class<T> responseType) {
        List<T> models;
        try (var reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<?> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(responseType)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withIgnoreEmptyLine(true)
                    .build();
            models = (List<T>) csvToBean.parse();
        } catch (Exception ex) {
            log.error("error parsing csv file {} ", ex);
            throw new IllegalArgumentException(ex.getCause().getMessage());
        }
        return models;
    }

    private List<Account> mapAccountCSVToModel(List<AccountCsvDTO> accountsCsvDTO) {
        return accountsCsvDTO.stream()
                .map(csv -> modelMapper.map(csv, Account.class))
                .collect(Collectors.toList());
    }

    private List<AccountGetDTO> mapAccountToAccountGetDTO(List<Account> account) {
        return account.stream()
                .map(csv -> modelMapper.map(csv, AccountGetDTO.class))
                .collect(Collectors.toList());
    }

    private AccountPageResponse<AccountGetDTO> mapAccountToResponse(Page<Account> accountPage)  {
        var pageResponse = new AccountPageResponse<AccountGetDTO>();
        pageResponse.setTotalPages(accountPage.getTotalPages());
        pageResponse.setTotalSize(accountPage.getTotalElements());
        pageResponse.setPage(accountPage.getNumber());
        pageResponse.setSize(accountPage.getSize());
        pageResponse.setContent(mapAccountToAccountGetDTO(accountPage.getContent()));
        return pageResponse;
    }




}
