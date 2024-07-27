package com.totvs.challenge.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "conta")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Column(name = "data_vencimento")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dueDate;

    @Column(name = "data_pagamento")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate payDay;

    @Column(name = "valor")
    private BigDecimal amount;

    @Column(name = "descricao")
    private String description;

    @Column(name = "situacao")
    private String status;

}
