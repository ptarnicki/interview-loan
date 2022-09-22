package com.loan.service;

import com.loan.api.rest.domain.LoanResponse;
import com.loan.api.rest.exception.LoanNotFoundException;
import com.loan.config.LoanProperties;
import com.loan.repository.model.Loan;
import com.loan.repository.model.LoanRepository;
import com.loan.validation.LoanValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanValidator loanValidator;
    private final LoanRepository loanRepository;
    private final LoanProperties loanProperties;

    @Transactional
    public LoanResponse applyLoan(LocalDate from, LocalDate to, BigDecimal amount) {
        validateLoan(from, to, amount);
        var loan = Loan.builder()
                .amount(amount)
                .from(from)
                .to(to)
                .interest(getInterest(amount))
                .build();
        var persistedLoan = loanRepository.save(loan);
        return new LoanResponse(persistedLoan.getId().toString(), persistedLoan.getAmount(), persistedLoan.getInterest(), persistedLoan.getTo());
    }

    public LoanResponse getLoanById(String id) {
        return loanRepository.findById(UUID.fromString(id))
                .map(loan -> new LoanResponse(loan.getId().toString(), loan.getAmount(), loan.getInterest(), loan.getTo()))
                .orElseThrow(() -> new LoanNotFoundException(String.format("Not found loan by id=%s", id)));
    }

    @Transactional
    public LoanResponse extendLoan(String id) {
        var loan = loanRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new LoanNotFoundException(String.format("Not found loan by id=%s", id)));
        var to = loan.getTo();
        var extendedTo = to.plusDays(loanProperties.getExtensionTerm());
        validateLoan(loan.getFrom(), extendedTo, loan.getAmount());
        loan.setTo(extendedTo);
        var extendedLoan = loanRepository.save(loan);
        return new LoanResponse(extendedLoan.getId().toString(), extendedLoan.getAmount(), extendedLoan.getInterest(), extendedLoan.getTo());
    }

    private BigDecimal getInterest(BigDecimal amount) {
        return amount.multiply(loanProperties.getInterest());
    }

    private void validateLoan(LocalDate from, LocalDate to, BigDecimal amount) {
        loanValidator.validateLoanTerm(from, to);
        loanValidator.validateLoanTermRange(from, to);
        loanValidator.validateLoanAmountRange(amount);
        loanValidator.validateOffHours(amount);
    }
}
