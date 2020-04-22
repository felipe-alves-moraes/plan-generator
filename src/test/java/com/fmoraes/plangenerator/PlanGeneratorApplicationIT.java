package com.fmoraes.plangenerator;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class PlanGeneratorApplicationIT {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldGenerateThePlanAsDocumentedOnChallengeDoc() throws Exception {
        mockMvc.perform(post("/generate-plan")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Files.readAllBytes(Paths.get("", "src/test/resources").resolve("payload.json"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", Matchers.hasSize(24)))
                .andExpect(jsonPath("$[0].borrowerPaymentAmount", Matchers.is("219.36")))
                .andExpect(jsonPath("$[0].date", Matchers.is("2020-01-01T00:00:01Z")))
                .andExpect(jsonPath("$[0].initialOutstandingPrincipal", Matchers.is("5000")))
                .andExpect(jsonPath("$[0].interest", Matchers.is("20.83")))
                .andExpect(jsonPath("$[0].principal", Matchers.is("198.53")))
                .andExpect(jsonPath("$[0].remainingOutstandingPrincipal", Matchers.is("4801.47")))
                .andExpect(jsonPath("$[1].borrowerPaymentAmount", Matchers.is("219.36")))
                .andExpect(jsonPath("$[1].date", Matchers.is("2020-02-01T00:00:01Z")))
                .andExpect(jsonPath("$[1].initialOutstandingPrincipal", Matchers.is("4801.47")))
                .andExpect(jsonPath("$[1].interest", Matchers.is("20.01")))
                .andExpect(jsonPath("$[1].principal", Matchers.is("199.35")))
                .andExpect(jsonPath("$[1].remainingOutstandingPrincipal", Matchers.is("4602.12")))
                .andExpect(jsonPath("$[-1].borrowerPaymentAmount", Matchers.is("219.28")))
                .andExpect(jsonPath("$[-1].date", Matchers.is("2021-12-01T00:00:01Z")))
                .andExpect(jsonPath("$[-1].initialOutstandingPrincipal", Matchers.is("218.37")))
                .andExpect(jsonPath("$[-1].interest", Matchers.is("0.91")))
                .andExpect(jsonPath("$[-1].principal", Matchers.is("218.37")))
                .andExpect(jsonPath("$[-1].remainingOutstandingPrincipal", Matchers.is("0")));
    }

    @Test
    public void shouldGenerateThePlanForAReallyShortPeriod() throws Exception {
        mockMvc.perform(post("/generate-plan")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Files.readAllBytes(Paths.get("", "src/test/resources").resolve("really-short-payload.json"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].borrowerPaymentAmount", Matchers.is("2.52")))
                .andExpect(jsonPath("$[0].date", Matchers.is("2020-01-01T00:00:01Z")))
                .andExpect(jsonPath("$[0].initialOutstandingPrincipal", Matchers.is("5")))
                .andExpect(jsonPath("$[0].interest", Matchers.is("0.02")))
                .andExpect(jsonPath("$[0].principal", Matchers.is("2.50")))
                .andExpect(jsonPath("$[0].remainingOutstandingPrincipal", Matchers.is("2.50")))
                .andExpect(jsonPath("$[1].borrowerPaymentAmount", Matchers.is("2.51")))
                .andExpect(jsonPath("$[1].date", Matchers.is("2020-02-01T00:00:01Z")))
                .andExpect(jsonPath("$[1].initialOutstandingPrincipal", Matchers.is("2.50")))
                .andExpect(jsonPath("$[1].interest", Matchers.is("0.01")))
                .andExpect(jsonPath("$[1].principal", Matchers.is("2.50")))
                .andExpect(jsonPath("$[1].remainingOutstandingPrincipal", Matchers.is("0")));
    }

    @Test
    public void shouldReceiveUnprocessableEntityWithInvalidDataPayload() throws Exception {
        mockMvc.perform(post("/generate-plan")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Files.readAllBytes(Paths.get("", "src/test/resources").resolve("invalid-payload.json"))))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldGetBadRequestStatus() throws Exception {
        mockMvc.perform(post("/generate-plan")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Files.readAllBytes(Paths.get("", "src/test/resources").resolve("invalid-payload-validation-error.json"))))
                .andExpect(status().isBadRequest());
    }
}