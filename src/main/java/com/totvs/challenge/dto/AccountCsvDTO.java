package com.totvs.challenge.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.opencsv.bean.CsvBindByName;

import com.opencsv.bean.CsvCustomBindByPosition;
import com.opencsv.bean.CsvDate;
import com.totvs.challenge.utils.LocalDateConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountCsvDTO {

    @CsvBindByName(column = "data_vencimento")
    @CsvDate(value = "dd-MM-uuuu")
    private LocalDate dueDate;

    @CsvBindByName(column = "data_pagamento")
    @CsvDate(value = "dd-MM-uuuu")
    private LocalDate payDay;

    @CsvBindByName(column = "valor")
    private BigDecimal amount;

    @CsvBindByName(column = "descricao")
    private String description;

    @CsvBindByName(column = "situacao")
    private String status;

}
