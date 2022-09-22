package com.loan.api.rest.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.loan.ser.MoneySerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequest {

    @FutureOrPresent(message = "{request.from.future.msg}")
    @NotNull(message = "{request.from.null.msg}")
    private LocalDate from;

    @Future(message = "{request.to.future.msg}")
    @NotNull(message = "{request.to.null.msg}")
    private LocalDate to;

    @Positive(message = "{request.amount.positive.msg}")
    @NotNull(message = "{request.amount.null.msg}")
    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal amount;
}
