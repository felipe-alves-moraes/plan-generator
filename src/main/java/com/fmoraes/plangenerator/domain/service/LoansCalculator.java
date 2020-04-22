package com.fmoraes.plangenerator.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Component
public class LoansCalculator {
    private static final Logger LOG = LoggerFactory.getLogger(LoansCalculator.class);

    private static final int MONTHS_IN_YEAR = 12;
    private static final int DAYS_IN_MONTH = 30;
    private static final int DAYS_IN_YEAR = 360;

    public BigDecimal calculateAnnuity(BigDecimal loanAmount, double interestRate, int duration) {
        if (loanAmount == null || interestRate == 0 || duration == 0) {
            return BigDecimal.ZERO;
        }

        double monthlyRate = getMonthlyRate(interestRate);
        double loanDivisor = 1 - (Math.pow(1 + monthlyRate, -duration));
        return loanAmount
                .multiply(BigDecimal.valueOf(monthlyRate))
                .divide(BigDecimal.valueOf(loanDivisor), 2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal calculateInterest(BigDecimal loanAmount, double interestRate) {
        if (loanAmount == null || interestRate == 0) {
            return BigDecimal.ZERO;
        }

        LOG.debug("Calculating Interest for values: loanAmount={} and interestRate={}", loanAmount, interestRate);
        BigDecimal interest = loanAmount
                .multiply(BigDecimal.valueOf(getDecimalRate(interestRate)))
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

    private double getMonthlyRate(double interestRate) {
        return getDecimalRate(interestRate) / MONTHS_IN_YEAR;
    }

    private double getDecimalRate(double interestRate) {
        return interestRate / 100;
    }
}
