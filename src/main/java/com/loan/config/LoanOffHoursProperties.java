package com.loan.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "loan.off-hours")
public class LoanOffHoursProperties {
    private String start;
    private String stop;

    public LocalTime getStart() {
        return LocalTime.parse(start);
    }

    public LocalTime getStop() {
        return LocalTime.parse(stop);
    }
}
