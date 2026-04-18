package com.vmsigma.paymentservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "payment-service");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/process")
    public ResponseEntity<Map<String, Object>> processPayment(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        response.put("transactionId", "TXN-" + System.currentTimeMillis());
        response.put("status", "SUCCESS");
        response.put("amount", request.get("amount"));
        response.put("currency", request.getOrDefault("currency", "INR"));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<Map<String, Object>> getPayment(@PathVariable String transactionId) {
        Map<String, Object> response = new HashMap<>();
        response.put("transactionId", transactionId);
        response.put("status", "COMPLETED");
        response.put("amount", 5000.00);
        response.put("currency", "INR");
        return ResponseEntity.ok(response);
    }
}
