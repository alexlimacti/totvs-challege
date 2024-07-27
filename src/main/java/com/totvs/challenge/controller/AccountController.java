package com.totvs.challenge.controller;

import com.totvs.challenge.dto.AccountDTO;
import com.totvs.challenge.dto.AccountFilterDTO;
import com.totvs.challenge.dto.AccountGetDTO;
import com.totvs.challenge.dto.AccountPageResponse;
import com.totvs.challenge.dto.AccountStatusDTO;
import com.totvs.challenge.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping(value = "/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "Cadastro de conta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conta cadastrada",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountDTO.class))
            })
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createAccount(@RequestBody AccountDTO accountDTO) {
        accountService.createAccount(accountDTO);
    }

    @Operation(summary = "Cadastro de contas por upload de arquivo CSV")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Contas cadastradas",
                    content = { @Content(mediaType = "application/csv",
                            schema = @Schema(implementation = AccountDTO.class))
                    })
    })
    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void uploadAccountFile(@RequestParam("file") MultipartFile file) throws IOException {
        accountService.readAndSaveCSVAccounts(file);
    }

    @Operation(summary = "Alteração de status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status alterado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada",
                    content = { @Content(mediaType = "application/json") })
    })
    @PostMapping("/change-status/{accountID}")
    @ResponseStatus(HttpStatus.OK)
    public void changeStatus(@PathVariable Long accountID, @Valid @RequestBody AccountStatusDTO accountStatusDTO) {
        accountService.changeStatus(accountID, accountStatusDTO);
    }

    @Operation(summary = "Atualização de conta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conta atualizada",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada",
                    content = { @Content(mediaType = "application/json") })
    })
    @PutMapping("/update/{accountID}")
    public void updateAccount(@PathVariable Long accountID, @Valid @RequestBody AccountDTO accountDTO) {
        accountService.updateAccount(accountID, accountDTO);
    }

    @Operation(summary = "Busca de conta por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conta encontrada e retornada",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountGetDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada",
                    content = { @Content(mediaType = "application/json") })
    })
    @GetMapping("/get-account/{accountID}")
    public ResponseEntity<AccountGetDTO> getAccount(@RequestParam Long accountID) {
        return ResponseEntity.ok(accountService.findAccountById(accountID));
    }

    @Operation(summary = "Busca de conta por período na data de vencimento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contas encontradas e retornadas",
                    content = { @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = AccountGetDTO.class)))
                    })
    })
    @GetMapping("/get-accounts-between-duedate/{dueDateInitial}/{dueDateFinal}")
    public ResponseEntity<List<AccountGetDTO>> getAccountsBetweenDueDate(@PathVariable @DateTimeFormat(pattern="dd-MM-yyyy") LocalDate dueDateInitial, @PathVariable @DateTimeFormat(pattern="dd-MM-yyyy") LocalDate dueDateFinal) {
        return ResponseEntity.ok(accountService.findAccountsByDueDateBetween(dueDateInitial, dueDateFinal));
    }

    @Operation(summary = "Busca de contas por período na data de pagamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contas encontradas e retornadas",
                    content = { @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = AccountGetDTO.class)))
                    })
    })
    @GetMapping("/get-accounts-between-payday/{paydayInitial}/{paydayFinal}")
    public ResponseEntity<List<AccountGetDTO>> getAccountsBetweenPayDay(@PathVariable @DateTimeFormat(pattern="dd-MM-yyyy") LocalDate paydayInitial, @PathVariable @DateTimeFormat(pattern="dd-MM-yyyy") LocalDate paydayFinal) {
        return ResponseEntity.ok(accountService.findAccountsByPayDayBetween(paydayInitial, paydayFinal));
    }

    @Operation(summary = "Busca de contas com filtro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contas encontradas e retornadas",
                    content = { @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = AccountGetDTO.class)))
                    })
    })
    @GetMapping("/get-accounts-with-filter")
    public ResponseEntity<AccountPageResponse<AccountGetDTO>> findByFilter(AccountFilterDTO accountFilterDTO) {
        return ResponseEntity.ok(accountService.findByFilter(accountFilterDTO, accountFilterDTO.getPage(), accountFilterDTO. getSize()));
    }

}
