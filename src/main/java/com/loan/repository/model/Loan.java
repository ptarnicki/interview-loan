package com.loan.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Loan {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "varbinary not null")
    private UUID id;

    @Column(name = "date_from")
    private LocalDate from;

    @Column(name = "date_to")
    private LocalDate to;

    @Column
    private BigDecimal amount;

    @Column
    private BigDecimal interest;
}
