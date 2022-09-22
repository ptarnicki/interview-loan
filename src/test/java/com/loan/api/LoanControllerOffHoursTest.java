package com.loan.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loan.api.rest.domain.LoanRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest({"spring.main.allow-bean-definition-overriding=true"})
@Import(LoanControllerOffHoursTest.OverrideBean.class)
@AutoConfigureMockMvc
class LoanControllerOffHoursTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class OverrideBean {
        @Bean
        @Primary
        Clock clock() {
            return Clock.fixed(Instant.parse("2022-09-20T03:15:30.00Z"), ZoneId.systemDefault());
        }
    }

    @Test
    void shouldValidateOffHours() throws Exception {
        //given
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(20);
        BigDecimal amount = new BigDecimal("999").setScale(2, RoundingMode.HALF_EVEN);
        LoanRequest loanRequest = new LoanRequest(start, end, amount);

        // when - then
        mockMvc
                .perform(
                        post("/loans")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loanRequest))
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

}
