package com.totvs.challenge.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Account DTO")
public class AccountGetDTO {

    @JsonProperty(value = "id")
    private Long id;

    @Schema(description = "Data de Vencimento", example = "01/10/2024", pattern = "dd-MM-yyyy")
    @JsonProperty(value = "data_vencimento")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dueDate;

    @Schema(description = "Data de Pagamento", example = "01/10/2024", pattern = "dd-MM-yyyy")
    @JsonProperty(value = "data_pagamento")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate payDay;

    @Schema(description = "Valor", example = "10.5")
    @JsonProperty(value = "valor")
    private BigDecimal amount;

    @Schema(description = "Descrição da conta")
    @JsonProperty(value = "descricao")
    private String description;

    @Schema(description = "Situação da conta")
    @JsonProperty(value = "situacao")
    private String status;

}
