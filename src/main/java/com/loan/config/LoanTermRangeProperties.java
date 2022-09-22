package com.loan.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "loan.term-range")
public class LoanTermRangeProperties {
    private Integer min;
    private Integer max;
}
