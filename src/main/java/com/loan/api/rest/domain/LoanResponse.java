package com.loan.api.rest.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.loan.ser.MoneySerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class LoanResponse {
    private String loanId;
    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal amount;
    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal interest;
    private LocalDate dueDate;
}
