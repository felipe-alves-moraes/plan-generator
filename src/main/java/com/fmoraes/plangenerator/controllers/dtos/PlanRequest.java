package com.fmoraes.plangenerator.controllers.dtos;

import com.fmoraes.plangenerator.controllers.validators.ValidDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

public class PlanRequest {

    @Pattern(regexp = "\\d+(\\.\\d+)?", message = "must be a positive number")
    @NotNull
    private String loanAmount;
    @Pattern(regexp = "\\d+(\\.\\d+)?", message = "must be a positive number")
    @NotNull
    private String nominalRate;
    @Positive
    @NotNull
    private int duration;
    @ValidDate
    @NotNull
    private String startDate;

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getNominalRate() {
        return nominalRate;
    }

    public void setNominalRate(String nominalRate) {
        this.nominalRate = nominalRate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
