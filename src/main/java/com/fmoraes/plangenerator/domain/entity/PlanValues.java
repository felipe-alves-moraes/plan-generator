package com.fmoraes.plangenerator.domain.entity;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public final class PlanValues {
    private final BigDecimal loanAmount;
    private final double interestRate;
    private final int duration;
    private final ZonedDateTime startDate;

    public PlanValues(BigDecimal loanAmount, double interestRate, int duration, ZonedDateTime startDate) {
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.duration = duration;
        this.startDate = startDate;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public int getDuration() {
        return duration;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    @Override
    public String toString() {
        return "PlanValues{" +
                "loanAmount=" + loanAmount +
                ", interestRate=" + interestRate +
                ", duration=" + duration +
                ", startDate=" + startDate +
                '}';
    }
}
