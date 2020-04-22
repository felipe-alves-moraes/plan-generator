package com.fmoraes.plangenerator.domain.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class LoansCalculatorTest {

    private final LoansCalculator loansCalculator = new LoansCalculator();

    /**
     * Annuity Payment Formula: Amount * rate / 1 - ((1 + rate) ^ -duration)
     */
    @Test
    public void shouldReturnAnnuityAccordingToAnnuityPaymentFormula() {
        BigDecimal result = loansCalculator.calculateAnnuity(BigDecimal.valueOf(5000), 5.0, 24);

        assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(219.36));
    }

    @Test
    public void shouldReturnZeroAnnuityIfLoanAmountIsNull() {
        BigDecimal result = loansCalculator.calculateAnnuity(null, 5.0, 24);

        assertThat(result).isEqualByComparingTo(BigDecimal.ZERO);
    }

    /**
     * Interest Formula: (Rate * Days in Month * Initial Outstanding Principal) / Days
     */
    @Test
    public void shouldReturnInterestAccordingToFormula() {
        BigDecimal result = loansCalculator.calculateInterest(BigDecimal.valueOf(5000), 5.0);

        assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(20.83));
    }

    @Test
    public void shouldReturnZeroInterestIfLoanAmountIsNull() {
        BigDecimal result = loansCalculator.calculateInterest(null, 5.0);

        assertThat(result).isEqualByComparingTo(BigDecimal.ZERO);
    }

    /**
     * Principal Formula = Annuity - Interest
     */
    @Test
    public void shouldReturnPrincipalAccordingToFormula() {
        BigDecimal result = loansCalculator.calculatePrincipal(BigDecimal.valueOf(219.36), BigDecimal.valueOf(20.83));

        assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(198.53));
    }

    @Test
    public void shouldReturnZeroPrincipalIfAnnuityIsNull() {
        BigDecimal result = loansCalculator.calculatePrincipal(null, BigDecimal.valueOf(20.83));

        assertThat(result).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    public void shouldReturnPrincipalIfInterestIsNull() {
        BigDecimal result = loansCalculator.calculatePrincipal(BigDecimal.valueOf(219.36), null);

        assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(219.36));
    }

    /**
     * Borrower Payment Amount Formula = Principal + Interest
     */
    @Test
    public void shouldReturnBorrowerPaymentAmountAccordingToFormula() {
        BigDecimal result = loansCalculator.calculateBorrowerPaymentAmount(BigDecimal.valueOf(198.53), BigDecimal.valueOf(20.83));

        assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(219.36));
    }

    @Test
    public void shouldReturnZeroBorrowerPaymentAmountIfPrincipalIsNull() {
        BigDecimal result = loansCalculator.calculateBorrowerPaymentAmount(null, BigDecimal.valueOf(20.83));

        assertThat(result).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    public void shouldReturnBorrowerPaymentAmountIfInterestIsNull() {
        BigDecimal result = loansCalculator.calculateBorrowerPaymentAmount(BigDecimal.valueOf(198.53), null);

        assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(198.53));
    }
}