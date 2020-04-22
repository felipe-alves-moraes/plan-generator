package com.fmoraes.plangenerator.controllers.mappers;

import com.fmoraes.plangenerator.controllers.dtos.PlanRequest;
import com.fmoraes.plangenerator.controllers.dtos.PlanResponse;
import com.fmoraes.plangenerator.domain.entity.PlanValues;
import com.fmoraes.plangenerator.domain.entity.RepaymentPlan;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public final class PlanGeneratorMapper {

    private PlanGeneratorMapper(){}

    public static PlanValues fromDTO(PlanRequest planRequest) {
        return new PlanValues(new BigDecimal(planRequest.getLoanAmount()),
                Double.parseDouble(planRequest.getNominalRate()), planRequest.getDuration(),
                ZonedDateTime.parse(planRequest.getStartDate()));
    }

    public static List<PlanResponse> toDTO(List<RepaymentPlan> repaymentPlans) {
        return repaymentPlans.stream().map(PlanGeneratorMapper::toDTO).collect(Collectors.toList());
    }

    private static PlanResponse toDTO(RepaymentPlan repaymentPlan) {
        return new PlanResponse(
                repaymentPlan.getDate(),
                repaymentPlan.getBorrowerPaymentAmount().toString(),
                repaymentPlan.getPrincipal().toString(),
                repaymentPlan.getInterest().toString(),
                repaymentPlan.getInitialOutstandingPrincipal().toString(),
                repaymentPlan.getRemainingOutstandingPrincipal().toString()
        );
    }
}
