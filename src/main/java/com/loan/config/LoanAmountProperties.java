package com.loan.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "loan.amount")
public class LoanAmountProperties {
    private BigDecimal min;
    private BigDecimal max;
}
