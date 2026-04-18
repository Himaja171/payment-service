package com.vmsigma.paymentservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void healthCheck_ShouldReturnUp() throws Exception {
        mockMvc.perform(get("/api/payments/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("payment-service"));
    }

    @Test
    void processPayment_ShouldReturnSuccess() throws Exception {
        String requestBody = """
                {
                    "amount": 5000,
                    "currency": "INR",
                    "accountNumber": "1234567890"
                }
                """;

        mockMvc.perform(post("/api/payments/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.transactionId").exists())
                .andExpect(jsonPath("$.amount").value(5000));
    }

    @Test
    void getPayment_ShouldReturnTransactionDetails() throws Exception {
        mockMvc.perform(get("/api/payments/TXN-123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("TXN-123456"))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }
}
