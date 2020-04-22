package com.fmoraes.plangenerator.service;

import com.fmoraes.plangenerator.TestData;
import com.fmoraes.plangenerator.domain.entity.PlanValues;
import com.fmoraes.plangenerator.domain.entity.RepaymentPlan;
import com.fmoraes.plangenerator.domain.service.LoansCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PlanGeneratorServiceTest {

    private final PlanGeneratorService planGeneratorService = new PlanGeneratorService(new LoansCalculator());

    @ParameterizedTest
    @MethodSource("parameterizedInvalidTestData")
    void shouldReceiveEmptyListWhenCreatingPlanWithZeroAsLoanAmount(PlanValues planValues) {
        List<RepaymentPlan> repaymentPlan = planGeneratorService.createRepaymentPlan(planValues);
        assertThat(repaymentPlan).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("parameterizedValidTestData")
    void shouldGeneratePlansSuccessfully(PlanValues planValues, TestResult result) {
        List<RepaymentPlan> repaymentPlan = planGeneratorService.createRepaymentPlan(planValues);
        assertThat(repaymentPlan).hasSize(result.size);
        assertThat(repaymentPlan.get(0).getDate()).isEqualTo(result.firstDate);
        assertThat(repaymentPlan.get(0).getBorrowerPaymentAmount()).isEqualByComparingTo(result.borrowedPaymentAmount);
        assertThat(repaymentPlan.get(0).getPrincipal()).isEqualByComparingTo(result.principal);
        assertThat(repaymentPlan.get(0).getInterest()).isEqualByComparingTo(result.interest);
        assertThat(repaymentPlan.get(0).getInitialOutstandingPrincipal()).isEqualByComparingTo(result.initialOutstandingAmount);
        assertThat(repaymentPlan.get(result.size - 1).getDate()).isEqualTo(result.lastDate);
        assertThat(repaymentPlan.get(result.size - 1).getRemainingOutstandingPrincipal()).isEqualByComparingTo(result.remainingOutstandingPrincipal);
    }

    private static List<Arguments> parameterizedValidTestData() {
        LocalDateTime dateTime = LocalDateTime.of(2020, 1, 1, 0, 0, 1);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneOffset.UTC);

        return Arrays.asList(
                Arguments.of(TestData.createPlanValues(5000, 5, 24),
                        new TestResult(24, zonedDateTime.toString(), zonedDateTime.plusMonths(23).toString(),
                                BigDecimal.valueOf(219.36),BigDecimal.valueOf(198.53), BigDecimal.valueOf(20.83), BigDecimal.valueOf(5000))),
                Arguments.of(TestData.createPlanValues(10000, 5, 12),
                        new TestResult(12, zonedDateTime.toString(), zonedDateTime.plusMonths(11).toString(),
                                BigDecimal.valueOf(856.07), BigDecimal.valueOf(814.40), BigDecimal.valueOf(41.67), BigDecimal.valueOf(10000))),
                Arguments.of(TestData.createPlanValues(15000, 10, 36),
                        new TestResult(36, zonedDateTime.toString(), zonedDateTime.plusMonths(35).toString(),
                                BigDecimal.valueOf(484.01), BigDecimal.valueOf(359.01), BigDecimal.valueOf(125.00), BigDecimal.valueOf(15000)))
        );
    }

    private static List<Arguments> parameterizedInvalidTestData() {
        LocalDateTime dateTime = LocalDateTime.of(2020, 1, 1, 0, 0, 1);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneOffset.UTC);

        return Arrays.asList(
                Arguments.of(TestData.createPlanValues(0, 5, 12)),
                Arguments.of(TestData.createPlanValues(5000, 0, 12)),
                Arguments.of(TestData.createPlanValues(5000, 5, 0))
        );
    }

    static class TestResult {
        public int size;
        public String firstDate;
        public String lastDate;
        public BigDecimal borrowedPaymentAmount;
        public BigDecimal principal;
        public BigDecimal interest;
        public BigDecimal initialOutstandingAmount;
        public BigDecimal remainingOutstandingPrincipal = BigDecimal.ZERO;

        public TestResult(int size) {
            this.size = size;
        }

        public TestResult(int size, String firstDate, String lastDate, BigDecimal borrowedPaymentAmount, BigDecimal principal, BigDecimal interest, BigDecimal initialOutstandingAmount) {
            this.size = size;
            this.firstDate = firstDate;
            this.lastDate = lastDate;
            this.borrowedPaymentAmount = borrowedPaymentAmount;
            this.principal = principal;
            this.interest = interest;
            this.initialOutstandingAmount = initialOutstandingAmount;
        }
    }
}