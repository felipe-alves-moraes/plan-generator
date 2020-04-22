package com.fmoraes.plangenerator.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Component
public class LoansCalculator {
    private static final Logger LOG = LoggerFactory.getLogger(LoansCalculator.class);

    private static final int MONTHS_IN_YEAR = 12;
    private static final int DAYS_IN_MONTH = 30;
    private static final int DAYS_IN_YEAR = 360;

    public BigDecimal calculateAnnuity(BigDecimal loanAmount, BigDecimal interestRate, int duration) {
        if (isNullOrZero(loanAmount) || isNullOrZero(interestRate) || duration == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal monthlyRate = getMonthlyRate(interestRate);
        BigDecimal loanDivisor = BigDecimal.ONE.subtract(monthlyRate.add(BigDecimal.ONE).pow(-duration, MathContext.DECIMAL32));
        return loanAmount
                .multiply(monthlyRate)
                .divide(loanDivisor, 2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal calculateInterest(BigDecimal loanAmount, BigDecimal interestRate) {
        if (isNullOrZero(loanAmount) || isNullOrZero(interestRate)) {
            return BigDecimal.ZERO;
        }

        LOG.debug("Calculating Interest for values: loanAmount={} and interestRate={}", loanAmount, interestRate);
        BigDecimal interest = loanAmount
                .multiply(getDecimalRate(interestRate))
                .multiply(BigDecimal.valueOf(DAYS_IN_MONTH))
                .divide(BigDecimal.valueOf(DAYS_IN_YEAR), 2, RoundingMode.HALF_EVEN);
        LOG.debug("Interest={}", interest);

        return interest;
    }

    public BigDecimal calculatePrincipal(BigDecimal annuity, BigDecimal interest) {
        if (annuity == null) {
            return BigDecimal.ZERO;
        }

        if (interest == null) {
            interest = BigDecimal.ZERO;
        }

        LOG.debug("Calculating Principal for values: annuity={} and interest={}", annuity, interest);
        BigDecimal principal = annuity.subtract(interest);
        LOG.debug("Principal={}", principal);

        return principal;
    }

    public BigDecimal calculateBorrowerPaymentAmount(BigDecimal principal, BigDecimal interest) {
        if (principal == null) {
            return BigDecimal.ZERO;
        }

        if (interest == null) {
            interest = BigDecimal.ZERO;
        }

        LOG.debug("Calculating BorrowerPaymentAmount for values: principal={} and interest={}", principal, interest);
        BigDecimal borrowerPaymentAmount = principal.add(interest);
        LOG.debug("BorrowerPaymentAmount={}", borrowerPaymentAmount);

        return borrowerPaymentAmount;
    }

    private BigDecimal getMonthlyRate(BigDecimal interestRate) {
        return getDecimalRate(interestRate).divide(BigDecimal.valueOf(MONTHS_IN_YEAR), 6, RoundingMode.HALF_EVEN);
    }

    private BigDecimal getDecimalRate(BigDecimal interestRate) {
        return interestRate.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_EVEN);
    }

    private boolean isNullOrZero(BigDecimal bigDecimal) {
        return bigDecimal == null || bigDecimal.compareTo(BigDecimal.ZERO) == 0;
    }
}
