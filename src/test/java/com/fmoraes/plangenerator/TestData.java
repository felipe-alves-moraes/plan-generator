package com.fmoraes.plangenerator;

import com.fmoraes.plangenerator.domain.entity.PlanValues;
import com.fmoraes.plangenerator.domain.entity.RepaymentPlan;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public final class TestData {
    private TestData(){}

    public static PlanValues createPlanValues(double amount, double rate, int duration) {
        LocalDateTime dateTime = LocalDateTime.of(2020, 1, 1, 0, 0, 1);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneOffset.UTC);

        return new PlanValues(BigDecimal.valueOf(amount), BigDecimal.valueOf(rate),
                duration, zonedDateTime);
    }
    
    public static RepaymentPlan createRepaymentPlan(double borrowerPaymentAmount, double principal, double interest,
                                                    double initialOutstandingPrincipal, double remainingOutstandingPrincipal) {
        LocalDateTime dateTime = LocalDateTime.of(2020, 1, 1, 0, 0, 1);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneOffset.UTC);
        
        return new RepaymentPlan(zonedDateTime, BigDecimal.valueOf(borrowerPaymentAmount), BigDecimal.valueOf(principal),
                BigDecimal.valueOf(interest), BigDecimal.valueOf(initialOutstandingPrincipal), BigDecimal.valueOf(remainingOutstandingPrincipal));
    }
}
