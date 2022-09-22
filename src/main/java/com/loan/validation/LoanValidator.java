package com.loan.validation;

import com.loan.api.rest.exception.LoanRequestException;
import com.loan.config.LoanAmountProperties;
import com.loan.config.LoanOffHoursProperties;
import com.loan.config.LoanTermRangeProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class LoanValidator {

    private final LoanAmountProperties loanAmountProperties;
    private final LoanTermRangeProperties loanTermRangeProperties;
    private final LoanOffHoursProperties loanOffHoursProperties;
    private final Clock clock;
    private final ZoneId zoneId;

    public void validateLoanTerm(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new LoanRequestException(String.format("Input dates are wrong for arguments: from=%s, to=%s", from, to));
        }
    }

    public void validateLoanTermRange(LocalDate from, LocalDate to) {
        var duration = Duration.between(from.atStartOfDay(), to.atStartOfDay()).toDays();
        if (loanTermRangeProperties.getMax() < duration) {
            throw new LoanRequestException(String.format("Invalid duration of loan=%s . Max duration is=%s", duration, loanTermRangeProperties.getMax()));
        }
        if (loanTermRangeProperties.getMin() > duration) {
            throw new LoanRequestException(String.format("Invalid duration of loan=%s . Min duration is=%s ", duration, loanTermRangeProperties.getMin()));
        }
    }

    public void validateLoanAmountRange(BigDecimal amount) {
        BigDecimal diffMax = loanAmountProperties.getMax().subtract(amount);
        if (diffMax.compareTo(BigDecimal.ZERO) < 0) {
            throw new LoanRequestException(String.format("Invalid loan amount range. Amount range is [min/max]=[%s,%s]", loanAmountProperties.getMin(), loanAmountProperties.getMax()));
        }

        BigDecimal diffMin = loanAmountProperties.getMin().subtract(amount);
        if (diffMin.compareTo(BigDecimal.ZERO) > 0) {
            throw new LoanRequestException(String.format("Invalid loan amount range. Amount range is [min/max]=[%s,%s]", loanAmountProperties.getMin(), loanAmountProperties.getMax()));
        }
    }

    public void validateOffHours(BigDecimal amount) {
        if (loanAmountProperties.getMax().compareTo(amount) == 0) {
            LocalTime now = LocalDateTime.ofInstant(clock.instant(), zoneId).toLocalTime();
            if (now.isAfter(loanOffHoursProperties.getStart()) && now.isBefore(loanOffHoursProperties.getStop())) {
                throw new LoanRequestException(String.format("Off hours for max amount. Off hours are [start/stop]=[%s,%s]", loanOffHoursProperties.getStart(), loanOffHoursProperties.getStop()));
            }
        }
    }
}
