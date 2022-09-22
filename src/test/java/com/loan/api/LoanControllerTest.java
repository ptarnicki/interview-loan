package com.loan.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loan.api.rest.domain.LoanRequest;
import com.loan.api.rest.domain.LoanResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateLoan() throws Exception {
        //given
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(20);
        BigDecimal amount = BigDecimal.TEN.setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal interest = amount.multiply(BigDecimal.valueOf(0.1)).setScale(2, RoundingMode.HALF_EVEN);
        LoanRequest loanRequest = new LoanRequest(start, end, amount);

        // when - then
        mockMvc
                .perform(
                        post("/loans")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loanRequest))
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.loanId").exists())
                .andExpect(jsonPath("$.amount").value(amount))
                .andExpect(jsonPath("$.interest").value(interest))
                .andExpect(jsonPath("$.dueDate").value(end.toString()));
    }

    @Test
    void shouldGetLoan() throws Exception {
        //given
        LocalDate start = LocalDate.now().plusDays(5);
        LocalDate end = LocalDate.now().plusDays(40);
        BigDecimal amount = BigDecimal.TEN.setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal interest = amount.multiply(BigDecimal.valueOf(0.1)).setScale(2, RoundingMode.HALF_EVEN);
        LoanRequest loanRequest = new LoanRequest(start, end, amount);

        // when - then
        var createResponse = mockMvc
                .perform(
                        post("/loans")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loanRequest))
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andReturn()
                .getResponse();

        var loanResponse = objectMapper.readValue(createResponse.getContentAsString(), LoanResponse.class);

        mockMvc
                .perform(get("/loans/".concat(loanResponse.getLoanId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.loanId").value(loanResponse.getLoanId()))
                .andExpect(jsonPath("$.amount").value(amount))
                .andExpect(jsonPath("$.interest").value(interest))
                .andExpect(jsonPath("$.dueDate").value(end.toString()));
    }

    @Test
    void shouldExtendLoan() throws Exception {
        //given
        LocalDate start = LocalDate.now().plusDays(5);
        LocalDate end = LocalDate.now().plusDays(40);
        BigDecimal amount = BigDecimal.TEN.setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal interest = amount.multiply(BigDecimal.valueOf(0.1)).setScale(2, RoundingMode.HALF_EVEN);
        LoanRequest loanRequest = new LoanRequest(start, end, amount);

        // when - then
        var createResponse = mockMvc
                .perform(
                        post("/loans")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loanRequest))
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andReturn()
                .getResponse();

        var loanResponse = objectMapper.readValue(createResponse.getContentAsString(), LoanResponse.class);

        mockMvc
                .perform(patch("/loans/".concat(loanResponse.getLoanId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.loanId").value(loanResponse.getLoanId()))
                .andExpect(jsonPath("$.amount").value(amount))
                .andExpect(jsonPath("$.interest").value(interest))
                .andExpect(jsonPath("$.dueDate").value(end.plusDays(20).toString()));
    }

    @Test
    void shouldValidateLoanTermInFuture() throws Exception {
        //given
        LocalDate start = LocalDate.now().plusDays(10);
        LocalDate end = LocalDate.now();
        BigDecimal amount = BigDecimal.TEN.setScale(2, RoundingMode.HALF_EVEN);
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

    @Test
    void shouldValidateLoanTerm() throws Exception {
        //given
        LocalDate start = LocalDate.now().plusDays(10);
        LocalDate end = LocalDate.now().plusDays(2);
        BigDecimal amount = BigDecimal.TEN.setScale(2, RoundingMode.HALF_EVEN);
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

    @Test
    void shouldValidateLoanTermRange() throws Exception {
        //given
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(110);
        BigDecimal amount = BigDecimal.TEN.setScale(2, RoundingMode.HALF_EVEN);
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

    @Test
    void shouldValidateLoanAmountRange() throws Exception {
        //given
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(20);
        BigDecimal amount = new BigDecimal("1500").setScale(2, RoundingMode.HALF_EVEN);
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

    @Test
    void shouldThrowNotFoundWhenGetLoan() throws Exception {
        // when - then
        mockMvc
                .perform(get("/loans/".concat(UUID.randomUUID().toString()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }
}
