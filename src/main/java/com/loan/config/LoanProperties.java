package com.loan.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "loan")
public class LoanProperties {
    private BigDecimal interest;
    private Integer extensionTerm;
}
