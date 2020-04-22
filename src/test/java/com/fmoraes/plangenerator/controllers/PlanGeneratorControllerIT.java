package com.fmoraes.plangenerator.controllers;

import com.fmoraes.plangenerator.domain.entity.RepaymentPlan;
import com.fmoraes.plangenerator.service.PlanGeneratorService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.fmoraes.plangenerator.TestData.createRepaymentPlan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PlanGeneratorController.class)
class PlanGeneratorControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlanGeneratorService planGeneratorService;

    @Test
    public void shouldReturnPlan() throws Exception {
        List<RepaymentPlan> repaymentPlan = Arrays.asList(createRepaymentPlan(219.36, 198.53,
                20.83, 5000, 4801.47),
                createRepaymentPlan(219.36, 199.35, 20.01,
                        4801.47, 4602.12));

        Mockito.when(planGeneratorService.createRepaymentPlan(Mockito.any()))
                .thenReturn(repaymentPlan);

        mockMvc.perform(post("/generate-plan")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Files.readAllBytes(Paths.get("", "src/test/resources").resolve("payload.json"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].borrowerPaymentAmount", Matchers.is("219.36")))
                .andExpect(jsonPath("$[0].principal", Matchers.is("198.53")))
                .andExpect(jsonPath("$[0].interest", Matchers.is("20.83")))
                .andExpect(jsonPath("$[0].initialOutstandingPrincipal", Matchers.is("5000.0")))
                .andExpect(jsonPath("$[0].remainingOutstandingPrincipal", Matchers.is("4801.47")))
                .andExpect(jsonPath("$[1].borrowerPaymentAmount", Matchers.is("219.36")))
                .andExpect(jsonPath("$[1].principal", Matchers.is("199.35")))
                .andExpect(jsonPath("$[1].interest", Matchers.is("20.01")))
                .andExpect(jsonPath("$[1].initialOutstandingPrincipal", Matchers.is("4801.47")))
                .andExpect(jsonPath("$[1].remainingOutstandingPrincipal", Matchers.is("4602.12")));
    }

    @Test
    public void shouldReturnEmptyPlan() throws Exception {
        Mockito.when(planGeneratorService.createRepaymentPlan(Mockito.any()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(post("/generate-plan")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Files.readAllBytes(Paths.get("", "src/test/resources").resolve("payload.json"))))
                .andExpect(status().isNoContent());
    }

}