package com.fmoraes.plangenerator.controllers;

import com.fmoraes.plangenerator.controllers.dtos.PlanRequest;
import com.fmoraes.plangenerator.controllers.dtos.PlanResponse;
import com.fmoraes.plangenerator.domain.entity.RepaymentPlan;
import com.fmoraes.plangenerator.service.PlanGeneratorService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static com.fmoraes.plangenerator.controllers.mappers.PlanGeneratorMapper.fromDTO;
import static com.fmoraes.plangenerator.controllers.mappers.PlanGeneratorMapper.toDTO;

@RestController
public class PlanGeneratorController {

    private final PlanGeneratorService planGeneratorService;

    public PlanGeneratorController(PlanGeneratorService planGeneratorService) {
        this.planGeneratorService = planGeneratorService;
    }

    @ApiResponse(responseCode = "204", description = "When it's not possible to create a plan")
    @ApiResponse(responseCode = "201", description = "When plan is created",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PlanResponse.class))))
    @PostMapping(value = "/generate-plan", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PlanResponse>> generatePlan(@Valid @RequestBody PlanRequest request) {
        List<RepaymentPlan> generatedPlan = planGeneratorService.createRepaymentPlan(fromDTO(request));

        return generatedPlan.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.created(null)
                        .body(toDTO(generatedPlan));
    }
}
