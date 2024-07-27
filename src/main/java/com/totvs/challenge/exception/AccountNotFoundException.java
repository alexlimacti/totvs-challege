package com.totvs.challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Account not found")
public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(Long accountID) {
        super("Account not found: " + accountID);
    }
}
