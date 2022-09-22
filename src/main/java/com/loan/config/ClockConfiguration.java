package com.loan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class ClockConfiguration {

    @Bean
    public Clock provideClock(ZoneId zoneId) {
        return Clock.system(zoneId);
    }

    @Bean
    public ZoneId provideZone() {
        return ZoneId.systemDefault();
    }

}
