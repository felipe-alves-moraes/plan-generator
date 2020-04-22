package com.fmoraes.plangenerator.controllers.dtos;

import java.time.ZonedDateTime;

public class PlanResponse {
    private final ZonedDateTime date;
    private final String borrowerPaymentAmount;
    private final String principal;
    private final String interest;
    private final String initialOutstandingPrincipal;
    private final String remainingOutstandingPrincipal;

    public PlanResponse(ZonedDateTime date, String borrowerPaymentAmount, String principal, String interest,
                        String initialOutstandingPrincipal, String remainingOutstandingPrincipal) {
        this.date = date;
        this.borrowerPaymentAmount = borrowerPaymentAmount;
        this.principal = principal;
        this.interest = interest;
        this.initialOutstandingPrincipal = initialOutstandingPrincipal;
        this.remainingOutstandingPrincipal = remainingOutstandingPrincipal;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public String getBorrowerPaymentAmount() {
        return borrowerPaymentAmount;
    }

    public String getPrincipal() {
        return principal;
    }

    public String getInterest() {
        return interest;
    }

    public String getInitialOutstandingPrincipal() {
        return initialOutstandingPrincipal;
    }

    public String getRemainingOutstandingPrincipal() {
        return remainingOutstandingPrincipal;
    }
}
