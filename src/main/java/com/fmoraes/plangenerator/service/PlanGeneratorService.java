package com.fmoraes.plangenerator.service;

import com.fmoraes.plangenerator.domain.entity.PlanValues;
import com.fmoraes.plangenerator.domain.entity.RepaymentPlan;
import com.fmoraes.plangenerator.domain.service.LoansCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PlanGeneratorService {
    private static final Logger LOG = LoggerFactory.getLogger(PlanGeneratorService.class);

    private final LoansCalculator loansCalculator;

    public PlanGeneratorService(LoansCalculator loansCalculator) {
        this.loansCalculator = loansCalculator;
    }

    public List<RepaymentPlan> createRepaymentPlan(PlanValues values) {
        LOG.info("Received plan to calculate: {}", values);

        BigDecimal annuity = loansCalculator.calculateAnnuity(values.getLoanAmount(),
                values.getInterestRate(), values.getDuration());
        LOG.debug("Annuity calculated: {}€", annuity);
        if (isEqualTo(annuity, BigDecimal.ZERO)) {
            LOG.info("Not possible to calculate repayment plan with Zero values");
            return Collections.emptyList();
        }

        BigDecimal outstandingAmount = values.getLoanAmount();
        List<RepaymentPlan> plan = new ArrayList<>();
        for (int i = 0; i < values.getDuration(); i++) {
            BigDecimal interest = loansCalculator.calculateInterest(outstandingAmount, values.getInterestRate());
            BigDecimal principal = loansCalculator.calculatePrincipal(annuity, interest);
            BigDecimal remaining = outstandingAmount.subtract(principal);

            if (i == values.getDuration() - 1 && isGreaterThan(remaining, BigDecimal.ZERO)) {
                LOG.debug("It's the last month of payment, and we still needs to receive: {}€", remaining);
                LOG.debug("Adding remaining to principal: {} + {}", principal, remaining);
                principal = principal.add(remaining.abs());
                remaining = BigDecimal.ZERO;
            }

            BigDecimal borrowerPaymentAmount = loansCalculator.calculateBorrowerPaymentAmount(principal, interest);

            if (isGreaterThan(principal, outstandingAmount)) {
                LOG.debug("Calculated principal exceeds outstanding amount, considering outstanding amount as principal");
                principal = outstandingAmount;
                borrowerPaymentAmount = borrowerPaymentAmount.subtract(remaining.abs());
                remaining = BigDecimal.ZERO;
            }

            plan.add(new RepaymentPlan(values.getStartDate().plusMonths(i),
                    borrowerPaymentAmount,
                    principal, interest,
                    outstandingAmount,
                    remaining));

            outstandingAmount = remaining;
        }

        LOG.debug("Generated plan: {}", plan);
        LOG.info("Plan generated!");
        return plan;
    }

    private boolean isGreaterThan(BigDecimal first, BigDecimal second) {
        return first.compareTo(second) > 0;
    }

    private boolean isEqualTo(BigDecimal first, BigDecimal second) {
        return first.compareTo(second) == 0;
    }

}
